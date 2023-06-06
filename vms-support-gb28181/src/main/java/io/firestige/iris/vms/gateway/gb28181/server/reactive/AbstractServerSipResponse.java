package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.gb28181.SipStatusCode;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public abstract class AbstractServerSipResponse implements ServerSipResponse {
    private enum State {NEW, COMMITTING, COMMIT_ACTION_FAILED, COMMITTED}
    private final DataBufferFactory factory;
    private SipStatusCode statusCode;
    private final SipHeaders headers;
    private final AtomicReference<State> state = new AtomicReference<>(State.NEW);
    private final List<Supplier<? extends Mono<Void>>> commitActions = new ArrayList<>(4);
    private SipHeaders readOnlyHeaders;

    public AbstractServerSipResponse(DataBufferFactory factory) {
        this(factory, new SipHeaders());
    }

    public AbstractServerSipResponse(DataBufferFactory factory, SipHeaders headers) {
        Assert.notNull(factory, "DataBufferFactory must not be null");
        Assert.notNull(headers, "HttpHeaders must not be null");
        this.factory = factory;
        this.headers = headers;
    }

    @Override
    public DataBufferFactory bufferFactory() {
        return factory;
    }

    @Override
    public boolean setStatusCode(SipStatusCode status) {
        if (this.state.get() == State.COMMITTED) {
            return false;
        }
        else {
            this.statusCode = status;
            return true;
        }
    }

    @Override
    public SipStatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public boolean setRawStatusCode(Integer code) {
        return setStatusCode(code != null ? SipStatusCode.valueOf(code) : null);
    }

    @Override
    public SipHeaders getHeaders() {
        if (this.readOnlyHeaders != null) {
            return this.readOnlyHeaders;
        }
        else if (this.state.get() == State.COMMITTED) {
            this.readOnlyHeaders = SipHeaders.readOnlySipHeaders(this.headers);
            return this.readOnlyHeaders;
        }
        else {
            return this.headers;
        }
    }

    public abstract <T> T getNativeResponse();

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {
        commitActions.add(action);
    }

    @Override
    public boolean isCommitted() {
        State state = this.state.get();
        return (state != State.NEW && state != State.COMMIT_ACTION_FAILED);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if (body instanceof Mono) {
            return ((Mono<? extends DataBuffer>) body)
                    .flatMap(buffer -> {
                        touchDataBuffer(buffer);
                        AtomicBoolean subscribed = new AtomicBoolean();
                        return doCommit(
                                () -> {
                                    try {
                                        return writeWithInternal(Mono.fromCallable(() -> buffer)
                                                .doOnSubscribe(s -> subscribed.set(true))
                                                .doOnDiscard(DataBuffer.class, DataBufferUtils::release));
                                    }
                                    catch (Throwable ex) {
                                        return Mono.error(ex);
                                    }
                                })
                                .doOnError(ex -> DataBufferUtils.release(buffer))
                                .doOnCancel(() -> {
                                    if (!subscribed.get()) {
                                        DataBufferUtils.release(buffer);
                                    }
                                });
                    })
                    .doOnError(t -> getHeaders().clearContentHeaders())
                    .doOnDiscard(DataBuffer.class, DataBufferUtils::release);
        }
        else {
            return new ChannelSendOperator<>(body, inner -> doCommit(() -> writeWithInternal(inner)))
                    .doOnError(t -> getHeaders().clearContentHeaders());
        }
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return null;
    }

    @Override
    public Mono<Void> setComplete() {
        return !isCommitted() ? doCommit() :Mono.empty();
    }

    protected Mono<Void> doCommit() {
        return doCommit(null);
    }

    protected Mono<Void> doCommit(Supplier<? extends Mono<Void>> writeAction) {
        Flux<Void> allActions = Flux.empty();
        if (this.state.compareAndSet(State.NEW, State.COMMITTING)) {
            if (!this.commitActions.isEmpty()) {
                allActions = Flux.concat(Flux.fromIterable(this.commitActions).map(Supplier::get))
                        .doOnError(ex -> {
                            if (this.state.compareAndSet(State.COMMITTING, State.COMMIT_ACTION_FAILED)) {
                                getHeaders().clearContentHeaders();
                            }
                        });
            }
        }
        else if (this.state.compareAndSet(State.COMMIT_ACTION_FAILED, State.COMMITTING)) {
            // Skip commit actions
        }
        else {
            return Mono.empty();
        }

        allActions = allActions.concatWith(Mono.fromRunnable(() -> {
            applyStatusCode();
            applyHeaders();
            this.state.set(State.COMMITTED);
        }));

        if (writeAction != null) {
            allActions = allActions.concatWith(writeAction.get());
        }

        return allActions.then();
    }

    protected abstract Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body);
    protected abstract Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body);
    protected abstract Mono<Void> applyStatusCode();
    protected abstract Mono<Void> applyHeaders();
    protected void touchDataBuffer() {
    }
}

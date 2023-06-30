package xyz.firestige.vmsx.support.gb28181.server;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.firestige.vmsx.support.gb28181.SipHeaders;
import xyz.firestige.vmsx.support.gb28181.SipStatusCode;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * AbstractServerSipResponse
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
public abstract class AbstractServerSipResponse implements ServerSipResponse {
    private enum Status {NEW, COMMITTING, COMMIT_ACTION_FAILED, COMMITTED, ABORTED}
    private final DataBufferFactory bufferFactory;
    @Nullable
    private SipStatusCode statusCode;
    private final SipHeaders headers;
    private final AtomicReference<Status> status = new AtomicReference<>(Status.NEW);
    private final List<Supplier<? extends Mono<Void>>> commitActions = new ArrayList<>(4);
    @Nullable
    private SipHeaders readOnlyHeaders;

    public AbstractServerSipResponse(DataBufferFactory bufferFactory) {
        this(bufferFactory, new SipHeaders());
    }

    public AbstractServerSipResponse(DataBufferFactory bufferFactory, SipHeaders headers) {
        Assert.notNull(bufferFactory, "DataBufferFactory must not be null");
        Assert.notNull(headers, "SipHeaders must not be null");
        this.bufferFactory = bufferFactory;
        this.headers = headers;
    }

    @Override
    public DataBufferFactory bufferFactory() {
        return bufferFactory;
    }

    @Override
    public boolean setStatusCode(SipStatusCode statusCode) {
        if (this.status.get() == Status.COMMITTED) {
            return false;
        } else {
            this.statusCode = statusCode;
            return true;
        }
    }

    @Nullable
    @Override
    public SipStatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public SipHeaders getHeaders() {
        if (readOnlyHeaders != null) {
            return readOnlyHeaders;
        } else if (status.get() == Status.COMMITTED) {
            this.readOnlyHeaders = SipHeaders.readOnlyHttpHeaders(headers);
            return readOnlyHeaders;
        } else {
            return headers;
        }
    }

    public abstract <T> T getNativeResponse();

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {
        this.commitActions.add(action);
    }

    @Override
    public boolean isCommitted() {
        EnumSet<Status> set = EnumSet.of(Status.COMMITTING, Status.COMMITTED, Status.ABORTED);
        return set.contains(this.status.get());
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if (body instanceof Mono<? extends DataBuffer> mono) {
            return mono
                    .flatMap(buffer -> {
                        touchDataBuffer(buffer);
                        AtomicBoolean subscribed = new AtomicBoolean();
                        return doCommit(
                                () -> {
                                    try {
                                        return writeWithInternal(Mono.fromCallable(() -> buffer))
                                                .doOnSubscribe(unused -> subscribed.set(true))
                                                .doOnDiscard(DataBuffer.class, DataBufferUtils::release);
                                    } catch (Throwable ex) {
                                        return Mono.error(ex);
                                    }
                                })
                                .doOnError(cause -> DataBufferUtils.release(buffer))
                                .doOnCancel(() -> {
                                    if (!subscribed.get()) {
                                        DataBufferUtils.release(buffer);
                                    }
                                });
                    })
                    .doOnError(cause -> getHeaders().clearContentHeaders())
                    .doOnDiscard(DataBuffer.class, DataBufferUtils::release);
        } else {
            return new ChannelSendOperator<>(body, inner -> doCommit(() -> writeWithInternal(inner)))
                    .doOnError(cause -> getHeaders().clearContentHeaders());
        }
    }

    @Override
    public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return new ChannelSendOperator<>(body, inner -> doCommit(() -> writeAndFlushWithInternal(inner)))
                .doOnError(cause -> getHeaders().clearContentHeaders());
    }

    @Override
    public Mono<Void> setComplete() {
        return !isCommitted() ? doCommit() : Mono.empty();
    }

    protected Mono<Void> doCommit() {
        return doCommit(null);
    }

    protected Mono<Void> doCommit(@Nullable Supplier<? extends Mono<Void>> action) {
        Flux<Void> allActions = Flux.empty();
        if (this.status.compareAndSet(Status.NEW, Status.COMMITTING)) {
            if (!this.commitActions.isEmpty()) {
                allActions = Flux.concat(Flux.fromIterable(this.commitActions).map(Supplier::get))
                        .doOnError(cause -> {
                            if (status.compareAndSet(Status.COMMITTING, Status.COMMIT_ACTION_FAILED)) {
                                getHeaders().clearContentHeaders();
                            }
                        });
            }
        } else if (status.compareAndSet(Status.COMMIT_ACTION_FAILED, Status.COMMITTING)) {
            // skip
        } else {
            return Mono.empty();
        }

        allActions = allActions.concatWith(Mono.fromRunnable(() -> {
            applyStatusCode();
            applyHeaders();
            this.status.set(Status.COMMITTED);
        }));
        if (action != null){
            allActions = allActions.concatWith(action.get());
        }
        return allActions.then();
    }

    protected abstract Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body);

    protected abstract Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body);

    protected abstract void applyStatusCode();

    protected abstract void applyHeaders();

    protected void touchDataBuffer(DataBuffer dataBuffer) {
        // ignore
    }
}

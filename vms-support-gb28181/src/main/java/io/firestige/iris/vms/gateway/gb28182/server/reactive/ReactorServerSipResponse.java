package io.firestige.iris.vms.gateway.gb28182.server.reactive;

import io.firestige.iris.vms.gateway.gb28182.SipStatusCode;
import io.firestige.iris.vms.gateway.gb28182.ZeroCopySipOutputMessage;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.file.Path;
import java.util.Optional;

//todo 待补全
public class ReactorServerSipResponse extends AbstractServerSipResponse implements ZeroCopySipOutputMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorServerSipResponse.class);
    private final HttpServerResponse response;

    public ReactorServerSipResponse(HttpServerResponse response, NettyDataBufferFactory bufferFactory) {
        super(bufferFactory, new SipHeaders(new NettyHeadersAdapter(response.responseHeaders())));
        Assert.notNull(response, "HttpServerResponse must not be null");
        this.response = response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getNativeResponse() {
        return (T) this.response;
    }

    @Override
    public SipStatusCode getStatusCode() {
        return Optional.ofNullable(super.getStatusCode())
                .orElseGet(() -> SipStatusCode.valueOf(this.response.status().code()));
    }

    @Override
    public Mono<Void> writeWith(Path file, long position, long count) {
        return null;
    }

    @Override
    protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body) {
        return null;
    }

    @Override
    protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return null;
    }

    @Override
    protected Mono<Void> applyStatusCode() {
        return null;
    }

    @Override
    protected Mono<Void> applyHeaders() {
        return null;
    }
}

package io.firestige.iris.vms.support.gb28181.server;

import io.firestige.iris.vms.support.gb28181.SipHeaders;
import io.firestige.iris.vms.support.gb28181.SipStatusCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ChannelOperationsId;
import reactor.netty.http.server.HttpServerResponse;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

import java.util.Optional;

/**
 * ReactorServerSipResponse
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
class ReactorServerSipResponse extends AbstractServerSipResponse {
    private static final Logger logger = LoggerFactory.getLogger(ReactorServerSipResponse.class);

    private final HttpServerResponse response;
    public ReactorServerSipResponse(HttpServerResponse httpServerResponse, NettyDataBufferFactory bufferFactory) {
        super(bufferFactory, new SipHeaders(new NettyHeadersAdapter(httpServerResponse.responseHeaders())));
        this.response = httpServerResponse;
    }

    @Override
    public <T> T getNativeResponse() {
        return (T) this.response;
    }

    @Override
    public SipStatusCode getStatusCode() {
        return Optional.ofNullable(super.getStatusCode())
                .orElseGet(() -> SipStatusCode.valueOf(this.response.status().code()));
    }

    @Override
    protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> body) {
        return this.response.send(toByteBuffs(body)).then();
    }

    @Override
    protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return this.response.sendGroups(Flux.from(body).map(this::toByteBuffs)).then();
    }

    @Override
    protected void applyStatusCode() {
        Optional.ofNullable(super.getStatusCode())
                .ifPresent(status -> this.response.status(status.value()));
    }

    @Override
    protected void applyHeaders() {
        //ignore
    }

    @Override
    protected void touchDataBuffer(DataBuffer dataBuffer) {
        if (logger.isDebugEnabled()) {
            if (response instanceof ChannelOperationsId operationsId) {
                DataBufferUtils.touch(dataBuffer, "Channel id: " + operationsId.asLongText());
            } else {
                this.response.withConnection(connection -> {
                    ChannelId id = connection.channel().id();
                    DataBufferUtils.touch(dataBuffer, "Channel id: " + id.asShortText());
                });
            }
        }
    }

    private Publisher<ByteBuf> toByteBuffs(Publisher<? extends DataBuffer> dataBuffers) {
        return dataBuffers instanceof Mono<? extends DataBuffer>
                ? Mono.from(dataBuffers).map(NettyDataBufferFactory::toByteBuf)
                : Flux.from(dataBuffers).map(NettyDataBufferFactory::toByteBuf);
    }
}

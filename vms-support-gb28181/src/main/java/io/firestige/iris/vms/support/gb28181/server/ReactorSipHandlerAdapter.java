package io.firestige.iris.vms.support.gb28181.server;

import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import org.springframework.core.io.buffer.NettyDataBufferFactory;

import java.util.function.BiFunction;

/**
 * SipHandlerAdapter
 *
 * @author firestige
 * @createAt 2023/6/13
 **/
public class ReactorSipHandlerAdapter implements BiFunction<HttpServerRequest, HttpServerResponse, Mono<Void>> {
    private final SipHandler handler;
    public ReactorSipHandlerAdapter(SipHandler handler) {
        this.handler = handler;
    }

    @Override
    public Mono<Void> apply(HttpServerRequest httpServerRequest, HttpServerResponse httpServerResponse) {
        NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(httpServerResponse.alloc());
        try {
            ReactorServerSipRequest request = new ReactorServerSipRequest(httpServerRequest, bufferFactory);
            ReactorServerSipResponse response = new ReactorServerSipResponse(httpServerResponse, bufferFactory);
            return handler.handle(request, response)
                    .doOnError(cause -> {})
                    .doOnSuccess(unused -> {});
        } catch (Exception e) {
            httpServerResponse.status(HttpResponseStatus.BAD_REQUEST);
            return Mono.empty();
        }
    }
}

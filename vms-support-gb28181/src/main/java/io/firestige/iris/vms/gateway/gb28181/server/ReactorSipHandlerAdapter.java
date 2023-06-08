package io.firestige.iris.vms.gateway.gb28181.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.function.BiFunction;

public class ReactorSipHandlerAdapter implements BiFunction<HttpServerRequest, HttpServerResponse, Mono<Void>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorSipHandlerAdapter.class);
    private final SipHandler sipHandler;

    public ReactorSipHandlerAdapter(SipHandler sipHandler) {
        Assert.notNull(sipHandler, "SipHandler must not be null");
        this.sipHandler = sipHandler;
    }

    @Override
    public Mono<Void> apply(HttpServerRequest reactorRequest, HttpServerResponse reactorResponse) {
        NettyDataBufferFactory factory = new NettyDataBufferFactory(reactorResponse.alloc());
        try {
            return this.sipHandler.handle()
                    // todo 可观测性，补充日志
                    .doOnError(cause -> LOGGER.trace(""))
                    .doOnSuccess(unused -> LOGGER.trace(""));
        } catch (Exception cause) {
            // todo 异常处理
            return Mono.empty();
        }
    }
}

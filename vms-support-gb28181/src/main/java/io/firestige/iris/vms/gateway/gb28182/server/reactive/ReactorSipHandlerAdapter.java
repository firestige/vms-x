package io.firestige.iris.vms.gateway.gb28182.server.reactive;

import io.firestige.iris.vms.gateway.sip.SipResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.net.URISyntaxException;
import java.util.function.BiFunction;

public class ReactorSipHandlerAdapter implements BiFunction<HttpServerRequest, HttpServerResponse, Mono<Void>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorSipHandlerAdapter.class);

    private final SipHandler handler;

    public ReactorSipHandlerAdapter(SipHandler handler) {
        Assert.notNull(handler, "SipHandler must not be null");
        this.handler = handler;
    }

    @Override
    public Mono<Void> apply(HttpServerRequest reactorRequest, HttpServerResponse reactorResponse) {
        NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(reactorResponse.alloc());
        try {
            ReactorServerSipRequest request = new ReactorServerSipRequest(reactorRequest, bufferFactory);
            ServerSipResponse response = new ReactorServerSipResponse(reactorResponse, bufferFactory);

            return this.handler.handle(request, response)
                    .doOnError(cause -> LOGGER.trace("{} Failed to complete: {}",
                            request.getLogPrefix(), cause.getMessage()))
                    .doOnSuccess(unused -> LOGGER.trace("{} Handling completed",
                            request.getLogPrefix()));
        } catch (URISyntaxException cause) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failed to get request URI: " + cause.getMessage());
            }
            reactorResponse.status(SipResponseStatus.BAD_REQUEST);
            return Mono.empty();
        }
    }
}

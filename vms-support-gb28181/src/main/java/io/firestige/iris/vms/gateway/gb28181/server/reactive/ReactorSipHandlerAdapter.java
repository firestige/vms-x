package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.sip.ServerSipResponse;
import io.firestige.iris.vms.gateway.sip.SipHandler;
import io.firestige.iris.vms.gateway.sip.SipResponseStatus;
import io.firestige.iris.vms.gateway.sip.SipServerRequest;
import io.firestige.iris.vms.gateway.sip.SipServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.function.BiFunction;

public class ReactorSipHandlerAdapter implements BiFunction<SipServerRequest, SipServerResponse, Mono<Void>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorSipHandlerAdapter.class);

    private final SipHandler handler;

    public ReactorSipHandlerAdapter(SipHandler handler) {
        Assert.notNull(handler, "SipHandler must not be null");
        this.handler = handler;
    }

    @Override
    public Mono<Void> apply(SipServerRequest reactorRequest, SipServerResponse reactorResponse) {
        NettyDataBufferFactory bufferFactory = new NettyDataBufferFactory(reactorResponse.alloc());
        try {
            ReactorServerSipRequest request = new ReactorServerSipRequest(reactorRequest, bufferFactory);
            ServerSipResponse response = new ReactorServerSipResponse(reactorResponse, bufferFactory);

            return this.handler.handle(request, response)
                    .doOnError(cause -> LOGGER.trace("{} Failed to complete: {}",
                            reactorRequest.getLogPrefix(), cause.getMessage()))
                    .doOnSuccess(unused -> LOGGER.trace("{} Handling completed".
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

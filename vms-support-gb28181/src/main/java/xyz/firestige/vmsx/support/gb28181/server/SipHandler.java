package xyz.firestige.vmsx.support.gb28181.server;

import reactor.core.publisher.Mono;

/**
 * SipHandler
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public interface SipHandler {
    Mono<Void> handle(ServerSipRequest request, ServerSipResponse response);
}

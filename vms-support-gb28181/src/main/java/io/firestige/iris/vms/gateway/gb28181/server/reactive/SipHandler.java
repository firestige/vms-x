package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import reactor.core.publisher.Mono;

public interface SipHandler {
    Mono<Void> handle(ServerSipRequest request, ServerSipResponse response);
}

package io.firestige.iris.vms.gateway.sip;

import reactor.core.publisher.Mono;

public interface SipHandler {
    Mono<Void> handle(ServerSipRequest request, ServerSipResponse response);
}

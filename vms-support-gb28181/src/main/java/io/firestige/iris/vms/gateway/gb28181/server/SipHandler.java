package io.firestige.iris.vms.gateway.gb28181.server;

import reactor.core.publisher.Mono;

public interface SipHandler {
    Mono<Void> handle();
}

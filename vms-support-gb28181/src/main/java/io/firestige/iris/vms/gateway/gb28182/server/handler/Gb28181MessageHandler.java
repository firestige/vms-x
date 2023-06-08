package io.firestige.iris.vms.gateway.gb28182.server.handler;

import io.firestige.iris.vms.gateway.gb28182.server.context.ServerExchange;
import reactor.core.publisher.Mono;

public interface Gb28181MessageHandler {
    Mono<Void> handle(ServerExchange exchange);
}

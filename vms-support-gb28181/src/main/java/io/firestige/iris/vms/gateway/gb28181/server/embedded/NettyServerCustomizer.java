package io.firestige.iris.vms.gateway.gb28181.server.embedded;

import reactor.netty.http.server.HttpServer;

import java.util.function.Function;

@FunctionalInterface
public interface NettyServerCustomizer extends Function<HttpServer, HttpServer> {
}

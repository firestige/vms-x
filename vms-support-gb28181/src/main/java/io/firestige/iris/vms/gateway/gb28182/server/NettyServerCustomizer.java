package io.firestige.iris.vms.gateway.gb28182.server;

import reactor.netty.http.server.HttpServer;

import java.util.function.Function;

@FunctionalInterface
public interface NettyServerCustomizer extends Function<HttpServer, HttpServer> {
}

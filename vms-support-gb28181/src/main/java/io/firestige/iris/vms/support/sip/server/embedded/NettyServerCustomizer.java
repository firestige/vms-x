package io.firestige.iris.vms.support.sip.server.embedded;

import reactor.netty.http.server.HttpServer;

import java.util.function.Function;

/**
 * NettyServerCustomizer
 *
 * @author firestige
 * @createAt 2023/6/13
 **/
@FunctionalInterface
public interface NettyServerCustomizer extends Function<HttpServer, HttpServer> {
}

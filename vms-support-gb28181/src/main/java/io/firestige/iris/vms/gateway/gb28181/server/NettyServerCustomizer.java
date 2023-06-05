package io.firestige.iris.vms.gateway.gb28181.server;

import io.firestige.iris.vms.gateway.sip.SipServer;

import java.util.function.Function;

@FunctionalInterface
public interface NettyServerCustomizer extends Function<SipServer, SipServer> {
}

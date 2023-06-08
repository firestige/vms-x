package io.firestige.iris.vms.gateway.gb28181.server;

@FunctionalInterface
public interface SipServerFactoryCustomizer<T extends SipServerFactory> {
    void customize(T server);
}

package io.firestige.iris.vms.gateway.gb28182.server;

import io.firestige.iris.vms.gateway.gb28182.server.reactive.SipHandler;

@FunctionalInterface
public interface ReactiveGb28181ServerFactory {
    Gb28181Server getGb28181Server(SipHandler sipHandler);
}

package io.firestige.iris.vms.gateway.gb28181.server;

import io.firestige.iris.core.server.ConfigurableVmsServerFactory;
import io.firestige.iris.core.server.VmsServerFactory;

public interface SipServerFactory extends VmsServerFactory, ConfigurableVmsServerFactory {
    SipServer getServer(SipHandler handler);
}

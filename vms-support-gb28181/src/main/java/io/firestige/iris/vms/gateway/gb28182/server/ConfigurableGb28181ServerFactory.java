package io.firestige.iris.vms.gateway.gb28182.server;

import org.springframework.boot.web.server.Shutdown;

import java.net.InetAddress;

public interface ConfigurableGb28181ServerFactory extends ReactiveGb28181ServerFactory {
    void setPort(int port);
    void setAddress(InetAddress address);
    void setServerHeader(String serverHeader);
    default void setShutdown(Shutdown shutdown) {

    }
}

package io.firestige.iris.core.server;

import java.net.InetAddress;

public abstract class AbstractConfigurableVmsServerFactory implements ConfigurableVmsServerFactory {
    private InetAddress address;
    private int port;
    private ShutdownStrategy shutdown = ShutdownStrategy.IMMEDIATE;

    public AbstractConfigurableVmsServerFactory() {
        this.port = 0;
    }

    public AbstractConfigurableVmsServerFactory(int port) {
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    public ShutdownStrategy getShutdown() {
        return shutdown;
    }

    public void setShutdown(ShutdownStrategy shutdown) {
        this.shutdown = shutdown;
    }
}

package io.firestige.iris.vms.gateway.gb28181.server;

import org.springframework.boot.web.server.Shutdown;

import java.net.InetAddress;

public abstract class AbstractReactiveGb28181ServerFactory implements ConfigurableGb28181ServerFactory {
    private int port;
    private InetAddress address;
    private String serverHeader;
    private Shutdown shutdown = Shutdown.IMMEDIATE;

    public AbstractReactiveGb28181ServerFactory() {
        this.port = 5061;
    }

    public AbstractReactiveGb28181ServerFactory(int port) {
        this.port = port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public void setServerHeader(String serverHeader) {
        this.serverHeader = serverHeader;
    }

    public String getServerHeader() {
        return serverHeader;
    }

    @Override
    public void setShutdown(Shutdown shutdown) {
        this.shutdown = shutdown;
    }

    public Shutdown getShutdown() {
        return shutdown;
    }
}

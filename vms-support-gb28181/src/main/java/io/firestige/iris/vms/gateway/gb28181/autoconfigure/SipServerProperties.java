package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import io.firestige.iris.core.server.ShutdownStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;

@ConfigurationProperties(prefix = "server.sip", ignoreInvalidFields = true)
public class SipServerProperties {
    private Integer port;
    private InetAddress address;
    private ShutdownStrategy shutdownStrategy;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public ShutdownStrategy getShutdown() {
        return shutdownStrategy;
    }

    public void setShutdown(ShutdownStrategy shutdownStrategy) {
        this.shutdownStrategy = shutdownStrategy;
    }
}

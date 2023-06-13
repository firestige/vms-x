package io.firestige.iris.vms.support.gb28181.autoconfigure;

import io.firestige.iris.core.server.ShutdownStrategy;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;

/**
 * GB28181Properties
 *
 * @author firestige
 * @createAt 2023/6/14
 **/
@ConfigurationProperties(prefix = "server.gb28181")
public class GB28181Properties {
    private Integer port;
    private InetAddress address;
    private ShutdownStrategy shutdownStrategy = ShutdownStrategy.IMMEDIATE;

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

    public ShutdownStrategy getShutdownStrategy() {
        return shutdownStrategy;
    }

    public void setShutdownStrategy(ShutdownStrategy shutdownStrategy) {
        this.shutdownStrategy = shutdownStrategy;
    }
}

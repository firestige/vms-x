package xyz.firestige.vmsx.core.server;

import java.net.InetAddress;

/**
 * 描述一些通用配置
 */
public interface ConfigurableVmsServerFactory extends VmsServerFactory {
    void setPort(int port);
    void setAddress(InetAddress address);
    default void setShutdownStrategy(ShutdownStrategy shutdown) {}
}

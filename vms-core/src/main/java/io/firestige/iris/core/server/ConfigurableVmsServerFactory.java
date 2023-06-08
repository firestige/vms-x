package io.firestige.iris.core.server;

import java.net.InetAddress;

/**
 * 描述一些通用配置
 */
public interface ConfigurableVmsServerFactory {
    void setPort(int port);
    void setAddress(InetAddress address);
    default void setShutdownStrategy(ShutdownStrategy shutdown) {}
}

package io.firestige.iris.vms.gateway.gb28182.server;

import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;

public interface Gb28181Server {
    void start() throws RuntimeException;
    void stop() throws RuntimeException;
    int getPort();
    default void shutDownGracefully(GracefulShutdownCallback callback) {
        callback.shutdownComplete(GracefulShutdownResult.IMMEDIATE);
    }
}

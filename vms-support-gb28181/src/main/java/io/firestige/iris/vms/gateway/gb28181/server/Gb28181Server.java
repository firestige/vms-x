package io.firestige.iris.vms.gateway.gb28181.server;

import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;

import java.util.concurrent.Future;

public interface Gb28181Server {
    void start() throws RuntimeException;
    void stop() throws RuntimeException;
    int getPort();
    default void shutDownGracefully(GracefulShutdownCallback callback) {
        callback.shutdownComplete(GracefulShutdownResult.IMMEDIATE);
    }
}

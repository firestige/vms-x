package io.firestige.iris.core.server;

/**
 * GracefulShutdownCallback
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
@FunctionalInterface
public interface GracefulShutdownCallback {
    void shutdownComplete(GracefullyShutdownStrategy strategy);
}

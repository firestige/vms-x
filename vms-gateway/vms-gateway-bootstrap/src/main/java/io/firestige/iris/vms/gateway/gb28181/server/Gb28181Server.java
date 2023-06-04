package io.firestige.iris.vms.gateway.gb28181.server;

import java.util.concurrent.Future;

public interface Gb28181Server {
    boolean isRunning();
    Future<Void> start();
    void shutdown();
}

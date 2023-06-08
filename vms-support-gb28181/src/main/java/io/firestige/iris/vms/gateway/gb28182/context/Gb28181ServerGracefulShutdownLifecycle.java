package io.firestige.iris.vms.gateway.gb28182.context;

import io.firestige.iris.vms.gateway.gb28182.server.Gb28181Server;
import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;

public final class Gb28181ServerGracefulShutdownLifecycle implements SmartLifecycle {
    public static final int SMART_LIFECYCLE_PHASE = SmartLifecycle.DEFAULT_PHASE - 1024;
    private final Gb28181Server server;
    private volatile boolean running;

    public Gb28181ServerGracefulShutdownLifecycle(Gb28181Server server) {
        this.server = server;
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Stop must not be invoked directly");
    }

    @Override
    public void stop(@NonNull Runnable callback) {
        this.running = false;
        this.server.shutDownGracefully(result ->callback.run());
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public int getPhase() {
        return SMART_LIFECYCLE_PHASE;
    }
}

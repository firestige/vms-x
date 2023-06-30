package xyz.firestige.vmsx.core.context;

import org.springframework.context.SmartLifecycle;
import org.springframework.lang.NonNull;

import xyz.firestige.vmsx.core.server.VmsServer;

/**
 * VmsServerGracefulShutdownLifecycle
 * 由优雅停机触发
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/13
 **/
public abstract class GenericVmsServerGracefulShutdownLifecycle implements SmartLifecycle {
    public static final int SMART_LIFECYCLE_PHASE = SmartLifecycle.DEFAULT_PHASE - 1024;
    private final VmsServer server;
    private volatile boolean running;

    protected GenericVmsServerGracefulShutdownLifecycle(VmsServer server) {
        this.server = server;
    }

    @Override
    public void start() {
        this.running = true;
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("stop must be invoked directly");
    }

    @Override
    public void stop(@NonNull Runnable callback) {
        this.running = false;
        this.server.shutdown(unused -> callback.run());
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

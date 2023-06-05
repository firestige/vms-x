package io.firestige.iris.vms.gateway.gb28181.context;

import org.springframework.context.SmartLifecycle;

class Gb28181ServerStartStopLifecycle implements SmartLifecycle {
    private final Gb28181ServerManager serverManager;
    private volatile boolean running;
    public Gb28181ServerStartStopLifecycle(Gb28181ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void start() {
        this.serverManager.start();
        this.running = false;
    }

    @Override
    public void stop() {
        this.running = false;
        this.serverManager.stop();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public int getPhase() {
        return Gb28181ServerGracefulShutdownLifecycle.SMART_LIFECYCLE_PHASE - 1024;
    }
}

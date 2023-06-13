package io.firestige.iris.vms.support.gb28181.context;

import org.springframework.context.SmartLifecycle;

/**
 * SipServerStartStopLifecycle
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/13
 **/
class GB28181ServerStartStopLifecycle implements SmartLifecycle {
    private final GB28181ServerManager serverManager;
    private volatile boolean running;

    public GB28181ServerStartStopLifecycle(GB28181ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    @Override
    public void start() {
        this.serverManager.start();
        this.running = true;
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
        return GB28181ServerGracefulShutdownLifecycle.SMART_LIFECYCLE_PHASE - 1024;
    }
}

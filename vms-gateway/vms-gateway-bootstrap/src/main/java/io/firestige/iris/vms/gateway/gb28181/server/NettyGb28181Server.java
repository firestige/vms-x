package io.firestige.iris.vms.gateway.gb28181.server;

import java.util.concurrent.Future;

public class NettyGb28181Server implements Gb28181Server {
    private volatile boolean isRunning;

    @Override
    public Future<Void> start() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shutdown'");
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
    
}

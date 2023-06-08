package io.firestige.iris.vms.gateway.gb28182.context;

import io.firestige.iris.vms.gateway.gb28182.server.Gb28181Server;
import org.springframework.context.ApplicationEvent;

public abstract class Gb28181ServerInitializedEvent extends ApplicationEvent {
    protected Gb28181ServerInitializedEvent(Gb28181Server server) {
        super(server);
    }

    @Override
    public Gb28181Server getSource() {
        return (Gb28181Server) super.getSource();
    }

    public Gb28181Server getServer() {
        return getSource();
    }

    public abstract Gb28181ApplicationContext getApplicationContext();
}

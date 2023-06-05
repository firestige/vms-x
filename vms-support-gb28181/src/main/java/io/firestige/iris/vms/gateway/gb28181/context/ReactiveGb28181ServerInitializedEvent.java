package io.firestige.iris.vms.gateway.gb28181.context;

import io.firestige.iris.vms.gateway.gb28181.server.Gb28181Server;

public class ReactiveGb28181ServerInitializedEvent extends Gb28181ServerInitializedEvent {
    private final ReactiveGb28181ServerApplicationContext ctx;
    public ReactiveGb28181ServerInitializedEvent(Gb28181Server server, ReactiveGb28181ServerApplicationContext ctx) {
        super(server);
        this.ctx = ctx;
    }

    @Override
    public ReactiveGb28181ServerApplicationContext getApplicationContext() {
        return this.ctx;
    }
}

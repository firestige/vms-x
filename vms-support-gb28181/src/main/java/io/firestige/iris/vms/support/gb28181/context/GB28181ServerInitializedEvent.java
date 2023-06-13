package io.firestige.iris.vms.support.gb28181.context;

import io.firestige.iris.vms.support.gb28181.server.SipServer;

import org.springframework.context.ApplicationEvent;

/**
 * GB28181ServerInitializedEvent
 *
 * @author firestige
 * @createAt 2023/6/11
 **/
public class GB28181ServerInitializedEvent extends ApplicationEvent {
    private final GB28181ApplicationContext ctx;

    public GB28181ServerInitializedEvent(SipServer server, GB28181ApplicationContext ctx) {
        super(server);
        this.ctx = ctx;
    }

    public GB28181ApplicationContext getApplicationContext() {
        return ctx;
    }

    public SipServer getSipServer() {
        return getSource();
    }

    @Override
    public SipServer getSource() {
        return (SipServer) super.getSource();
    }
}

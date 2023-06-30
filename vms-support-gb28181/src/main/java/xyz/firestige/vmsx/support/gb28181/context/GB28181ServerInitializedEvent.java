package xyz.firestige.vmsx.support.gb28181.context;

import org.springframework.context.ApplicationEvent;

import xyz.firestige.vmsx.support.gb28181.server.SipServer;

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

package io.firestige.iris.vms.gateway.gb28181.server;

import io.firestige.iris.vms.gateway.gb28181.server.reactive.ReactorSipHandlerAdapter;
import io.firestige.iris.vms.gateway.gb28181.server.reactive.SipHandler;
import io.firestige.iris.vms.gateway.sip.SipServer;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class NettyReactiveGb28181ServerFactory extends AbstractReactiveGb28181ServerFactory {
    private Set<NettyServerCustomizer> serverCustomizers = new LinkedHashSet<>();
    private Duration lifecycleTimeout;
    private Shutdown shutdown;

    public NettyReactiveGb28181ServerFactory() {
    }

    public NettyReactiveGb28181ServerFactory(int port) {
        super(port);
    }

    @Override
    public Gb28181Server getGb28181Server(SipHandler sipHandler) {
        SipServer server = createSipServer();
        ReactorSipHandlerAdapter handlerAdapter = new ReactorSipHandlerAdapter(sipHandler);
        NettyGb28181Server gb28181Server = createNettyGb28181Server(server, handlerAdapter, this.lifecycleTimeout
                , getShutdown());
        return gb28181Server;
    }

    private NettyGb28181Server createNettyGb28181Server(SipServer sipServer, ReactorSipHandlerAdapter handlerAdapter,
            Duration lifecycleTimeout, Shutdown shutdown) {
        return new NettyGb28181Server(sipServer, handlerAdapter, lifecycleTimeout, shutdown);
    }

    public Collection<NettyServerCustomizer> getServerCustomizers() {
        return serverCustomizers;
    }

    public void setServerCustomizers(Collection<? extends NettyServerCustomizer> serverCustomizers) {
        Assert.notNull(serverCustomizers, "ServerCustomizers must not be null");
        this.serverCustomizers = new LinkedHashSet<>(serverCustomizers);
    }

    public void addServerCustomizers(NettyServerCustomizer... serverCustomizers) {
        Assert.notNull(serverCustomizers, "ServerCustomizer must not be null");
        this.serverCustomizers.addAll(Arrays.asList(serverCustomizers));
    }

    public void setLifecycleTimeout(Duration lifecycleTimeout) {
        this.lifecycleTimeout = lifecycleTimeout;
    }

    @Override
    public void setShutdown(Shutdown shutdown) {
        this.shutdown = shutdown;
    }

    @Override
    public Shutdown getShutdown() {
        return this.shutdown;
    }

    private SipServer createSipServer() {
        SipServer server = SipServer.create();
        server = server.bindAddress(this::getListenAddress);
        return applyCustomizers(server);
    }

    private InetSocketAddress getListenAddress() {
        if (getAddress() != null) {
            return new InetSocketAddress(getAddress().getHostAddress(), getPort());
        }
        return new InetSocketAddress(getPort());
    }

    private SipServer applyCustomizers(SipServer server) {
        this.serverCustomizers.forEach(customizer -> customizer.apply(server));
        return server;
    }
}

package io.firestige.iris.vms.gateway.gb28181.server.embedded;

import io.firestige.iris.core.server.AbstractConfigurableVmsServerFactory;
import io.firestige.iris.core.server.ShutdownStrategy;
import io.firestige.iris.vms.gateway.gb28181.server.ReactorSipHandlerAdapter;
import io.firestige.iris.vms.gateway.gb28181.server.SipHandler;
import io.firestige.iris.vms.gateway.gb28181.server.SipServer;
import io.firestige.iris.vms.gateway.gb28181.server.SipServerFactory;
import io.firestige.iris.vms.gateway.gb28181.support.ReactorResourceFactory;
import org.springframework.util.Assert;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class NettySipServerFactory extends AbstractConfigurableVmsServerFactory implements SipServerFactory {
    private Set<NettyServerCustomizer> customizers = new LinkedHashSet<>();
    private Duration lifecycleTimeout;
    private ReactorResourceFactory factory;
    private ShutdownStrategy shutdown;

    public NettySipServerFactory() {
    }

    public NettySipServerFactory(int port) {
        super(port);
    }

    @Override
    public SipServer getServer(SipHandler handler) {
        HttpServer server = createHttpServer();
        ReactorSipHandlerAdapter handlerAdapter = new ReactorSipHandlerAdapter(handler);
        return new NettySipServer(server, handlerAdapter, lifecycleTimeout, getShutdown());
    }

    private HttpServer createHttpServer() {
        HttpServer server = HttpServer.create();
        if (factory != null) {
            LoopResources resources = this.factory.getLoopResources();
            Assert.notNull(resources, "No LoopResources: is ReactorResourceFactory not initialized yet?");
            server = server.runOn(resources).bindAddress(this::getListenAddress);
        } else {
            server = server.bindAddress(this::getListenAddress);
        }
        return applyCustomizers(server);
    }

    public Collection<NettyServerCustomizer> getCustomizers() {
        return customizers;
    }

    public void setCustomizers(Collection<? extends NettyServerCustomizer> customizers) {
        Assert.notNull(customizers, "ServerCustomizers must not be null");
        this.customizers = new LinkedHashSet<>(customizers);
    }

    public void addServerCustomizers(NettyServerCustomizer... customizers) {
        Assert.notNull(customizers, "ServerCustomizers must not be null");
        this.customizers.addAll(Arrays.asList(customizers));
    }

    public void setResourceFactory(ReactorResourceFactory factory) {
        this.factory = factory;
    }

    private InetSocketAddress getListenAddress() {
        if (getAddress() != null) {
            return new InetSocketAddress(getAddress().getHostAddress(), getPort());
        }
        return new InetSocketAddress(getPort());
    }

    private HttpServer applyCustomizers(HttpServer server) {
        for (NettyServerCustomizer customizer : this.customizers) {
            server = customizer.apply(server);
        }
        return server;
    }
}

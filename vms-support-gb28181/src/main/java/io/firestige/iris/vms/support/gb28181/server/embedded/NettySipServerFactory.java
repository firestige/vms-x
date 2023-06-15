package io.firestige.iris.vms.support.gb28181.server.embedded;

import io.firestige.iris.core.server.ShutdownStrategy;
import io.firestige.iris.vms.support.gb28181.GB28181ResourceFactory;
import io.firestige.iris.vms.support.gb28181.server.AbstractSipServerFactory;
import io.firestige.iris.vms.support.gb28181.server.ReactorSipHandlerAdapter;
import io.firestige.iris.vms.support.gb28181.server.SipHandler;
import io.firestige.iris.vms.support.gb28181.server.SipServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * NettySipServerFactory
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public class NettySipServerFactory extends AbstractSipServerFactory {
    private Set<NettyServerCustomizer> serverCustomizers = new LinkedHashSet<>();
    private Duration lifecycleTimeout;
    private GB28181ResourceFactory resourceFactory;
    private ShutdownStrategy shutdown;

    public NettySipServerFactory() {
        super(5061);
    }

    public NettySipServerFactory(int port) {
        super(port);
    }

    @Override
    public SipServer getSipServer(SipHandler handler) {
        HttpServer httpServer = createHttpServer();
        ReactorSipHandlerAdapter handlerAdapter = new ReactorSipHandlerAdapter(handler);
        return createNettySipServer(httpServer, handlerAdapter, this.lifecycleTimeout, getShutdown());
    }

    private HttpServer createHttpServer() {
        HttpServer server = HttpServer.create();
        if (this.resourceFactory != null) {
            LoopResources resources = this.resourceFactory.getLoopResources();
            server = server.runOn(resources).bindAddress(this::getListenAddress);
        } else {
            server = server.bindAddress(this::getListenAddress);
        }
        return applyCustomizers(server);
    }

    private SipServer createNettySipServer(HttpServer httpServer, ReactorSipHandlerAdapter handlerAdapter, Duration lifecycleTimeout, ShutdownStrategy shutdown) {
        return new NettySipServer(httpServer, handlerAdapter, lifecycleTimeout, shutdown);
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

    private InetSocketAddress getListenAddress() {
        return Optional.ofNullable(getAddress())
                .map(addr -> new InetSocketAddress(addr.getHostAddress(), getPort()))
                .orElseGet(() -> new InetSocketAddress(getPort()));
    }

    public void setLifecycleTimeout(Duration lifecycleTimeout) {
        this.lifecycleTimeout = lifecycleTimeout;
    }

    public void setResourceFactory(GB28181ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    @Override
    public void setShutdown(ShutdownStrategy shutdown) {
        this.shutdown = shutdown;
    }

    @Override
    public ShutdownStrategy getShutdown() {
        return shutdown;
    }

    private HttpServer applyCustomizers(HttpServer server) {
        for (NettyServerCustomizer customizer : this.serverCustomizers) {
            server = customizer.apply(server);
        }
        return server;
    }
}

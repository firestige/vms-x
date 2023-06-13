package io.firestige.iris.vms.support.sip.server.embedded;

import io.firestige.iris.core.server.ShutdownStrategy;
import io.firestige.iris.vms.support.gb28181.GB28181ResourceFactory;
import io.firestige.iris.vms.support.sip.server.AbstractSipServerFactory;
import io.firestige.iris.vms.support.sip.server.SipHandler;
import io.firestige.iris.vms.support.sip.server.SipServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
    }

    public NettySipServerFactory(int port) {
        super(port);
    }

    @Override
    public SipServer getSipServer(SipHandler handler) {
        HttpServer httpServer = createHttpServer();
        SipHandlerAdapter handlerAdapter = new SipHandlerAdapter(handler);
        return createNettySipServer(httpServer, handlerAdapter, this.lifecycleTimeout, getShutdown());
    }

    private HttpServer createHttpServer() {
        HttpServer server = HttpServer.create();
        if (this.resourceFactory != null) {
            LoopResources resources = this.resourceFactory.getLoopResource();
            server.runOn(resources).bindAddress(this::getListenAddress);
        } else {
            server = server.bindAddress(this::getListenAddress);
        }
        return applyCustomizers(server);
    }

    private SipServer createNettySipServer(HttpServer httpServer, SipHandlerAdapter handlerAdapter, Duration lifecycleTimeout, ShutdownStrategy shutdown) {
        return new NettySipServer(httpServer, handlerAdapter, lifecycleTimeout, shutdown);
    }

    private InetSocketAddress getListenAddress() {
        return Optional.ofNullable(getAddress())
                .map(addr -> new InetSocketAddress(addr.getHostAddress(), getPort()))
                .orElseGet(() -> new InetSocketAddress(getPort()));
    }

    private HttpServer applyCustomizers(HttpServer server) {
        for (NettyServerCustomizer customizer : this.serverCustomizers) {
            server = customizer.apply(server);
        }
        return null;
    }
}

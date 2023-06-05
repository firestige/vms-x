package io.firestige.iris.vms.gateway.sip;

import io.netty.channel.EventLoopGroup;
import io.netty.resolver.AddressResolverGroup;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.ConnectionObserver;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpResources;
import reactor.netty.transport.TransportConfig;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class SipResources implements ConnectionProvider, LoopResources {
    final LoopResources                            defaultLoops;
    final ConnectionProvider                       defaultProvider;
    final AtomicReference<AddressResolverGroup<?>> defaultResolver;

    protected SipResources(LoopResources defaultLoops, ConnectionProvider defaultProvider) {
        this.defaultLoops = defaultLoops;
        this.defaultProvider = defaultProvider;
        this.defaultResolver = new AtomicReference<>();
    }

    @Override
    public Mono<? extends Connection> acquire(
            TransportConfig config,
            ConnectionObserver observer,
            Supplier<? extends SocketAddress> remoteAddress,
            AddressResolverGroup<?> resolverGroup) {
        requireNonNull(config, "config");
        requireNonNull(observer, "observer");
        return defaultProvider.acquire(config, observer, remoteAddress, resolverGroup);
    }

    @Override
    public boolean daemon() {
        return defaultLoops.daemon();
    }

    @Override
    public void dispose() {

    }

    @Override
    public EventLoopGroup onServer(boolean useNative) {
        return null;
    }
}

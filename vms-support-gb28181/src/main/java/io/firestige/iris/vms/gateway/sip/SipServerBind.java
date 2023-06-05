package io.firestige.iris.vms.gateway.sip;

import io.netty.channel.ChannelOption;
import reactor.netty.internal.util.MapUtils;
import reactor.netty.tcp.TcpServerConfig;
import reactor.netty.udp.UdpServerConfig;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class SipServerBind extends SipServer {
    static final SipServerBind INSTANCE = new SipServerBind();
    final SipServerConfig config;

    SipServerBind() {
        Map<ChannelOption<?>, Boolean> childOptions = new HashMap<>(MapUtils.calculateInitialCapacity(2));
        childOptions.put(ChannelOption.AUTO_READ, false);
        childOptions.put(ChannelOption.TCP_NODELAY, true);
        this.config = new SipServerConfig(
                Collections.singletonMap(ChannelOption.SO_REUSEADDR, true),
                childOptions,
                () -> new InetSocketAddress(DEFAULT_PORT));
    }

    public SipServerBind(SipServerConfig config) {
        this.config = config;
    }

    @Override
    public SipServerConfig configuration() {
        return config;
    }

    @Override
    protected SipServer duplicate() {
        return new SipServerBind(new SipServerConfig(config));
    }

    static SipServer applyTcpServerConfig(TcpServerConfig config) {
        SipServer server =
                create().childObserve(config.childObserver())
                        .doOnChannelInit(config.doOnChannelInit())
                        .observe(config.connectionObserver())
                        .runOn(config.loopResources(), config.isPreferNative());
        if (Objects.nonNull(config.bindAddress())) {
            server = server.bindAddress(config.bindAddress());
        }
        if (Objects.nonNull(config.channelGroup())) {
            server = server.channelGroup(config.channelGroup());
        }
        return server;
    }

    static final int DEFAULT_PORT = 0;
}

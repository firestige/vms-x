package io.firestige.iris.vms.gateway.sip;

import io.firestige.iris.vms.gateway.gb28181.server.reactive.SipHeaders;
import reactor.netty.NettyInbound;
import reactor.util.annotation.Nullable;

import java.net.InetSocketAddress;
import java.time.ZonedDateTime;

public interface SipServerRequest extends NettyInbound, SipServerInfos {
    @Nullable
    @Override
    InetSocketAddress hostAddress();

    @Nullable
    @Override
    InetSocketAddress remoteAddress();

    SipHeaders requestHeaders();

    String protocol();

    ZonedDateTime timestamp();
}

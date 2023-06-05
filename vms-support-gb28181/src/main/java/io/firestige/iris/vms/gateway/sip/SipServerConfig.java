package io.firestige.iris.vms.gateway.sip;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LoggingHandler;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.channel.ChannelMetricsRecorder;
import reactor.netty.http.server.logging.AccessLog;
import reactor.netty.http.server.logging.AccessLogArgProvider;
import reactor.netty.resources.LoopResources;
import reactor.netty.transport.ServerTransportConfig;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SipServerConfig extends ServerTransportConfig<SipServerConfig> {

    boolean accessLogEnabled;
    Function<AccessLogArgProvider, AccessLog> accessLog;
    TransportType TransportType;
    SipRequestDecoderSpec decoder;
    Duration idleTimeout;
    BiFunction<? super Mono<Void>, ? super Connection, ? extends Mono<Void>> mapHandle;

    SipServerConfig(Map<ChannelOption<?>, ?> options, Map<ChannelOption<?>, ?> childOptions, Supplier<? extends SocketAddress> bindAddress) {
        super(options, childOptions, bindAddress);
    }

    SipServerConfig(SipServerConfig parent) {
        super(parent);
    }

    @Override
    protected LoggingHandler defaultLoggingHandler() {
        return null;
    }

    @Override
    protected LoopResources defaultLoopResources() {
        return null;
    }

    @Override
    protected ChannelMetricsRecorder defaultMetricsRecorder() {
        return null;
    }
}

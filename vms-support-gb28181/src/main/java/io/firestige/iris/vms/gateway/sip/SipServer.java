package io.firestige.iris.vms.gateway.sip;

import io.netty.channel.group.ChannelGroup;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.ConnectionObserver;
import reactor.netty.http.server.HttpRequestDecoderSpec;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.HttpServerState;
import reactor.netty.http.server.logging.AccessLogFactory;
import reactor.netty.transport.ServerTransport;
import reactor.util.context.Context;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static reactor.netty.ReactorNetty.format;

public abstract class SipServer extends ServerTransport<SipServer, SipServerConfig> {
    static final Logger LOGGER = LoggerFactory.getLogger(SipServer.class);
    public static SipServer create() {
        return SipServerBind.INSTANCE;
    }

    public final SipServer accessLog(boolean enable) {
        SipServer dup = duplicate();
        dup.configuration().accessLog = null;
        dup.configuration().accessLogEnabled = enable;
        return dup;
    }

    public final SipServer accessLog(boolean enable, AccessLogFactory accessLogFactory) {
        Objects.requireNonNull(accessLogFactory);
        SipServer dup = duplicate();
        dup.configuration().accessLog = enable ? accessLogFactory : null;
        dup.configuration().accessLogEnabled = enable;
        return dup;
    }

    @Override
    public final SipServer bindAddress(Supplier<? extends SocketAddress> bindAddressSupplier) {
        return super.bindAddress(bindAddressSupplier);
    }

    @Override
    public final SipServer channelGroup(ChannelGroup channelGroup) {
        return super.channelGroup(channelGroup);
    }

    public final SipServer handle(
            BiFunction<? super SipServerRequest, ? super SipServerResponse, ? extends Publisher<Void>> handler) {
        Objects.requireNonNull(handler, "handler");
        return childObserve(new SipServerHandle(handler));
    }

    @Override
    public final SipServer host(String host) {
        return super.host(host);
    }

    public final SipServer sipRequestDecoder(Function<SipRequestDecoderSpec, SipRequestDecoderSpec> requestDecoderOptions) {
        Objects.requireNonNull(requestDecoderOptions, "requestDecoderOptions");
        SipRequestDecoderSpec decoder = requestDecoderOptions.apply(new SipRequestDecoderSpec()).build();
        if (decoder.equals(configuration().decoder)) {
            return this;
        }
        SipServer dup = duplicate();
        dup.configuration().decoder = decoder;
        return dup;
    }

    public final SipServer idleTimeout(Duration idleTimeout) {
        Objects.requireNonNull(idleTimeout, "idleTimeout");
        SipServer dup = duplicate();
        dup.configuration().idleTimeout = idleTimeout;
        return dup;
    }

    public final SipServer mapHandle(BiFunction<? super Mono<Void>, ? super Connection, ? extends Mono<Void>> mapHandle) {
        Objects.requireNonNull(mapHandle, "mapHandle");
        SipServer dup = duplicate();
        dup.configuration().mapHandle = mapHandle;
        return dup;
    }

    @Override
    public final SipServer port(int port) {
        return super.port(port);
    }

    record SipServerHandle(
            BiFunction<? super SipServerRequest, ? super SipServerResponse, ? extends Publisher<Void>> handler
    ) implements ConnectionObserver {

        @Override
            public void onStateChange(Connection connection, State newState) {
                if (newState == HttpServerState.REQUEST_RECEIVED) {
                    try {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(format(connection.channel(), "Handler is being applied: {}"), handler);
                        }
                        SipServerOperations ops = (SipServerOperations) connection;
                        Publisher<Void> publisher = handler.apply(ops, ops);
                        Mono<Void> mono = Mono.deferContextual(ctx -> {
                            ops.currentContext = Context.of(ctx);
                            return Mono.fromDirect(publisher);
                        });
                        if (ops.mapHandle != null) {
                            mono = ops.mapHandle.apply(mono, connection);
                        }
                        mono.subscribe(ops.disposeSubscriber());
                    } catch (Throwable t) {
                        LOGGER.error(format(connection.channel(), ""), t);
                        connection.channel()
                                .close();
                    }
                }
            }
        }
}

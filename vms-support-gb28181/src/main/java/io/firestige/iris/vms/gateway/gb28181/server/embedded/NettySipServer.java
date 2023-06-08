package io.firestige.iris.vms.gateway.gb28181.server.embedded;

import io.firestige.iris.core.server.ServerException;
import io.firestige.iris.core.server.ShutdownStrategy;
import io.firestige.iris.vms.gateway.gb28181.server.ReactorSipHandlerAdapter;
import io.firestige.iris.vms.gateway.gb28181.server.SipServer;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class NettySipServer implements SipServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettySipServer.class);
    private final HttpServer httpServer;
    private final BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> handler;
    private final Duration lifecycleTimeout;
    private volatile DisposableServer disposableServer;

    public NettySipServer(
            HttpServer httpServer,
            ReactorSipHandlerAdapter handler,
            Duration lifecycleTimeout,
            ShutdownStrategy shutdown) {
        Assert.notNull(httpServer, "HttpServer must not be null");
        Assert.notNull(handler, "HandlerAdapter must not be null");
        this.httpServer = httpServer.channelGroup(new DefaultChannelGroup(new DefaultEventExecutor()));
        this.handler = handler;
        this.lifecycleTimeout = lifecycleTimeout;
    }

    @Override
    public synchronized void start() throws ServerException {
        if (Objects.nonNull(disposableServer)) {
            LOGGER.info("Netty is already started on " + disposableServer.port());
            return;
        }
        try {
            HttpServer server = this.httpServer.handle(handler);
            this.disposableServer = Optional.ofNullable(lifecycleTimeout)
                            .map(server::bindNow)
                            .orElseGet(server::bindNow);
        } catch (Exception ex) {
            throw new ServerException("Unable to start Netty", ex);
        }
    }

    @Override
    public synchronized void stop() throws ServerException {
        if (Objects.isNull(disposableServer)) {
            LOGGER.info("Netty has not been started yet");
            return;
        }
        try {
            Optional.ofNullable(this.lifecycleTimeout)
                    .ifPresentOrElse(this.disposableServer::disposeNow, this.disposableServer::disposeNow);
        } catch (Exception ex) {
            // 忽略
        }
        this.disposableServer = null;
    }

    @Override
    public int getPort() {
        return Optional.ofNullable(this.disposableServer)
                .map(disposable -> {
                    try {
                        return disposable.port();
                    } catch (UnsupportedOperationException ex) {
                        return -1;
                    }
                })
                .orElse(-1);
    }
}

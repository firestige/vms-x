package io.firestige.iris.vms.support.gb28181.server.embedded;

import io.firestige.iris.core.server.ServerException;
import io.firestige.iris.core.server.ShutdownStrategy;
import io.firestige.iris.vms.support.gb28181.server.ReactorSipHandlerAdapter;
import io.firestige.iris.vms.support.gb28181.server.SipServer;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * NettySipServer
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public class NettySipServer implements SipServer {
    private final HttpServer server;
    private final BiFunction<? super HttpServerRequest, ? super HttpServerResponse, ? extends Publisher<Void>> handler;
    private final Duration lifecycleTimeout;
    private final GracefulShutdown shutdown;
    private volatile DisposableServer disposableServer;
    public NettySipServer(
            HttpServer httpServer,
            ReactorSipHandlerAdapter handlerAdapter,
            Duration lifecycleTimeout,
            ShutdownStrategy shutdown) {
        this.lifecycleTimeout = lifecycleTimeout;
        this.handler = handlerAdapter;
        this.server = httpServer.channelGroup(new DefaultChannelGroup(new DefaultEventExecutor()));
        this.shutdown = shutdown == ShutdownStrategy.GRACEFUL
                ? new GracefulShutdown(() -> this.disposableServer) : null;
    }

    @Override
    public void start() throws ServerException {
        if (this.disposableServer == null) {
            try {
                this.disposableServer = startHttpServer();
            } catch (Exception ex) {
                // todo: add more error processor
                throw new ServerException("Unable to start server", ex);
            }
            // todo: add started log
            startDaemonAwaitThread();
        }
    }

    private DisposableServer startHttpServer() {
        HttpServer server = this.server.handle(this.handler);
        return Optional.ofNullable(this.lifecycleTimeout)
                .map(server::bindNow)
                .orElseGet(server::bindNow);
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> this.disposableServer.onDispose().block(), "sip-server");
        awaitThread.setDaemon(true);
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.start();
    }

    @Override
    public void stop() throws ServerException {
        if (this.disposableServer != null) {
            if (this.shutdown != null) {
                this.shutdown.abort();
            }
            try {
                if (this.lifecycleTimeout != null) {
                    this.disposableServer.disposeNow(this.lifecycleTimeout);
                }
                else {
                    this.disposableServer.disposeNow();
                }
            }
            catch (IllegalStateException ex) {
                // Continue
            }
            this.disposableServer = null;
        }
    }

    @Override
    public int getPort() {
        if (this.disposableServer != null) {
            try {
                return this.disposableServer.port();
            }
            catch (UnsupportedOperationException ex) {
                return -1;
            }
        }
        return -1;
    }
}

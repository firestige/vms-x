package xyz.firestige.vmsx.support.gb28181.server.embedded;

import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import xyz.firestige.vmsx.core.server.GracefulShutdownCallback;
import xyz.firestige.vmsx.core.server.GracefullyShutdownStrategy;
import xyz.firestige.vmsx.core.server.ServerException;
import xyz.firestige.vmsx.core.server.ShutdownStrategy;
import xyz.firestige.vmsx.support.gb28181.server.ReactorSipHandlerAdapter;
import xyz.firestige.vmsx.support.gb28181.server.SipServer;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * NettySipServer
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public class NettySipServer implements SipServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettySipServer.class);
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
            if (this.disposableServer != null) {
                LOGGER.info("Netty started" + getStartedOnMessage(this.disposableServer));
            }
            // todo: add started log
            startDaemonAwaitThread();
        }
    }

    private String getStartedOnMessage(DisposableServer server) {
        StringBuilder message = new StringBuilder();
        tryAppend(message, "port %s", server::port);
        tryAppend(message, "path %s", server::path);
        return (message.length() > 0) ? " on " + message : "";
    }

    private void tryAppend(StringBuilder message, String format, Supplier<Object> supplier) {
        try {
            Object value = supplier.get();
            message.append((message.length() != 0) ? " " : "");
            message.append(String.format(format, value));
        }
        catch (UnsupportedOperationException ex) {
        }
    }

    private DisposableServer startHttpServer() {
        HttpServer server = this.server.handle(this.handler);
        return Optional.ofNullable(this.lifecycleTimeout)
                .map(server::bindNow)
                .orElseGet(server::bindNow);
    }

    @Override
    public void shutdown(Consumer<GracefullyShutdownStrategy> callback) {
        if (this.shutdown == null) {
            callback.accept(GracefullyShutdownStrategy.IMMEDIATE);
            return;
        }
        this.shutdown.shutdownGracefully(callback::accept);
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> this.disposableServer.onDispose().block(), "sip-server");
        awaitThread.setDaemon(false);
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

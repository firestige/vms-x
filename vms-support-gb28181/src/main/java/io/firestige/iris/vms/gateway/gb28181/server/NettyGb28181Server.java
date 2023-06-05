package io.firestige.iris.vms.gateway.gb28181.server;

import io.firestige.iris.vms.gateway.gb28181.server.reactive.ReactorSipHandlerAdapter;
import io.firestige.iris.vms.gateway.sip.SipServer;
import io.firestige.iris.vms.gateway.sip.SipServerRequest;
import io.firestige.iris.vms.gateway.sip.SipServerResponse;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.unix.Errors;
import io.netty.util.concurrent.DefaultEventExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reactivestreams.Publisher;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;
import org.springframework.boot.web.server.PortInUseException;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.util.Assert;
import reactor.netty.ChannelBindException;
import reactor.netty.DisposableServer;

import java.time.Duration;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NettyGb28181Server implements Gb28181Server {
    private static final int ERROR_NO_EACCES = -13;
    private static final Log logger = LogFactory.getLog(NettyGb28181Server.class);

    private final SipServer sipServer;

    private final BiFunction<? super SipServerRequest, ? super SipServerResponse, ? extends Publisher<Void>> handler;

    private final Duration lifecycleTimeout;

    private final GracefulShutdown gracefulShutdown;

    private volatile DisposableServer disposableServer;

    public NettyGb28181Server(SipServer sipServer, ReactorSipHandlerAdapter handlerAdapter, Duration lifecycleTimeout,
                              Shutdown shutdown) {
        Assert.notNull(sipServer, "SipServer must not be null");
        Assert.notNull(handlerAdapter, "HandlerAdapter must not be null");
        this.sipServer = sipServer.channelGroup(new DefaultChannelGroup(new DefaultEventExecutor()));
        this.handler = handlerAdapter;
        this.lifecycleTimeout = lifecycleTimeout;
        this.gracefulShutdown = (shutdown == Shutdown.GRACEFUL) ? new GracefulShutdown(() -> this.disposableServer) :
                null;
    }

    @Override
    public void start() throws RuntimeException {
        if (Objects.isNull(this.disposableServer)) {
            try {
                this.disposableServer = startSipServer();
            } catch (Exception ex) {
                PortInUseException.ifCausedBy(ex, ChannelBindException.class, cause -> {
                    if (cause.localPort() > 0 && !isPermissionDenied(cause.getCause())) {
                        throw new PortInUseException(cause.localPort(), cause);
                    }
                });
                throw new Gb28181ServerException("Unable to start Netty", ex);
            }
            if (Objects.nonNull(this.disposableServer)) {
                logger.info("Netty started" + getStartedOnMessage(this.disposableServer));
            }
            startDaemonAwaitThread(this.disposableServer);
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
        } catch (UnsupportedOperationException ignored) {
        }
    }

    private DisposableServer startSipServer() {
        SipServer server = this.sipServer;
        server = server.handle(this.handler);
        if (Objects.nonNull(this.lifecycleTimeout)) {
            return server.bindNow(this.lifecycleTimeout);
        }
        return server.bindNow();
    }

    private boolean isPermissionDenied(Throwable bindExceptionCause) {
        try {
            if (bindExceptionCause instanceof Errors.NativeIoException nativeException) {
                return nativeException.expectedErr() == ERROR_NO_EACCES;
            }
        }
        catch (Throwable ignore) {
        }
        return false;
    }

    @Override
    public void shutDownGracefully(GracefulShutdownCallback callback) {
        if (this.gracefulShutdown == null) {
            callback.shutdownComplete(GracefulShutdownResult.IMMEDIATE);
            return;
        }
        this.gracefulShutdown.shutDownGracefully(callback);
    }

    private void startDaemonAwaitThread(DisposableServer disposableServer) {
        Thread awaitThread = new Thread("server") {

            @Override
            public void run() {
                disposableServer.onDispose().block();
            }

        };
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void stop() throws RuntimeException {
        if (Objects.nonNull(this.disposableServer)) {
            if (Objects.nonNull(this.gracefulShutdown)) {
                this.gracefulShutdown.abort();
            }
            try {
                if (Objects.nonNull(this.lifecycleTimeout)) {
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
        if (Objects.nonNull(this.disposableServer)) {
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

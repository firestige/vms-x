package io.firestige.iris.vms.gateway.gb28182.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.boot.web.server.GracefulShutdownResult;
import reactor.netty.DisposableServer;

import java.time.Duration;
import java.util.function.Supplier;

public class GracefulShutdown {

    private static final Log logger = LogFactory.getLog(GracefulShutdown.class);

    private final Supplier<DisposableServer> disposableServer;

    private volatile Thread shutdownThread;

    private volatile boolean shuttingDown;

    GracefulShutdown(Supplier<DisposableServer> disposableServer) {
        this.disposableServer = disposableServer;
    }

    void shutDownGracefully(GracefulShutdownCallback callback) {
        DisposableServer server = this.disposableServer.get();
        if (server == null) {
            return;
        }
        logger.info("Commencing graceful shutdown. Waiting for active requests to complete");
        this.shutdownThread = new Thread(() -> doShutdown(callback, server), "netty-shutdown");
        this.shutdownThread.start();
    }

    private void doShutdown(GracefulShutdownCallback callback, DisposableServer server) {
        this.shuttingDown = true;
        try {
            server.disposeNow(Duration.ofNanos(Long.MAX_VALUE));
            logger.info("Graceful shutdown complete");
            callback.shutdownComplete(GracefulShutdownResult.IDLE);
        }
        catch (Exception ex) {
            logger.info("Graceful shutdown aborted with one or more requests still active");
            callback.shutdownComplete(GracefulShutdownResult.REQUESTS_ACTIVE);
        }
        finally {
            this.shutdownThread = null;
            this.shuttingDown = false;
        }
    }

    void abort() {
        Thread shutdownThread = this.shutdownThread;
        if (shutdownThread != null) {
            while (!this.shuttingDown) {
                sleep(50);
            }
            shutdownThread.interrupt();
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

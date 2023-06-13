package io.firestige.iris.vms.support.gb28181.server.embedded;

import static io.firestige.iris.core.server.GracefullyShutdownStrategy.AFTER_ALL_REQUEST_FINISHED;
import static io.firestige.iris.core.server.GracefullyShutdownStrategy.IN_PERIOD;

import io.firestige.iris.core.server.GracefulShutdownCallback;
import reactor.netty.DisposableServer;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * GracefulShutdown
 *
 * @author firestige
 * @createAt 2023/6/14
 **/
final class GracefulShutdown {
    private final Supplier<DisposableServer> serverSupplier;
    private volatile Thread shutdownThread;
    private volatile boolean shuttingDown;

    GracefulShutdown(Supplier<DisposableServer> serverSupplier) {
        this.serverSupplier = serverSupplier;
    }

    void shutdownGracefully(GracefulShutdownCallback callback) {
        Optional.ofNullable(this.serverSupplier.get())
                .map(server -> new Thread(() -> doShutdown(callback, server), "netty-shutdown"))
                .ifPresent(Thread::start);
    }

    private void doShutdown(GracefulShutdownCallback callback, DisposableServer server) {
        this.shuttingDown = true;
        try {
            server.disposeNow(Duration.ofNanos(Long.MAX_VALUE));
            callback.shutdownComplete(AFTER_ALL_REQUEST_FINISHED);
        }
        catch (Exception ex) {
            callback.shutdownComplete(IN_PERIOD);
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
                try {
                    Thread.sleep(50);
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            shutdownThread.interrupt();
        }
    }
}

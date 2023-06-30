package xyz.firestige.vmsx.support.gb28181.context;

import reactor.core.publisher.Mono;
import xyz.firestige.vmsx.core.server.GracefulShutdownCallback;
import xyz.firestige.vmsx.support.gb28181.server.ServerSipRequest;
import xyz.firestige.vmsx.support.gb28181.server.ServerSipResponse;
import xyz.firestige.vmsx.support.gb28181.server.SipHandler;
import xyz.firestige.vmsx.support.gb28181.server.SipServer;
import xyz.firestige.vmsx.support.gb28181.server.SipServerFactory;

import java.util.function.Supplier;

/**
 * SipServerManager
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
class GB28181ServerManager {
    private final GB28181ApplicationContext ctx;
    private final DelayedInitializationSipHandler handler;
    private final SipServer server;

    public GB28181ServerManager(GB28181ApplicationContext ctx, SipServerFactory factory,
                                Supplier<SipHandler> handlerSupplier, boolean lazyInit) {
        this.ctx = ctx;
        this.handler = new DelayedInitializationSipHandler(handlerSupplier, lazyInit);
        this.server = factory.getSipServer(this.handler);
    }

    void start() {
        this.handler.initializeHandler();
        this.server.start();
        this.ctx.publishEvent(new GB28181ServerInitializedEvent(this.server, this.ctx));
    }

    void shutDownGracefully(GracefulShutdownCallback callback) {
        this.server.shutdown(callback::shutdownComplete);
    }

    void stop() {
        this.server.stop();
    }

    SipHandler getHandler() {
        return handler;
    }

    SipServer getServer() {
        return server;
    }

    static final class DelayedInitializationSipHandler implements SipHandler {
        private final Supplier<SipHandler> handlerSupplier;
        private final boolean lazyInit;
        private volatile SipHandler delegate = this::handleUninitialized;

        private DelayedInitializationSipHandler(Supplier<SipHandler> handlerSupplier, boolean lazyInit) {
            this.handlerSupplier = handlerSupplier;
            this.lazyInit = lazyInit;
        }

        private Mono<Void> handleUninitialized(ServerSipRequest request, ServerSipResponse response) {
            throw new IllegalStateException("The SipHandler has not yet been initialized");
        }

        void initializeHandler() {
            this.delegate = this.lazyInit ? new LazySipHandler(this.handlerSupplier) : this.handlerSupplier.get();
        }

        @Override
        public Mono<Void> handle(ServerSipRequest request, ServerSipResponse response) {
            return this.delegate.handle(request, response);
        }

        SipHandler getHandler() {
            return delegate;
        }
    }

    private static final class LazySipHandler implements SipHandler {
        private final Mono<SipHandler> delegate;

        public LazySipHandler(Supplier<SipHandler> supplier) {
            this.delegate = Mono.fromSupplier(supplier);
        }

        @Override
        public Mono<Void> handle(ServerSipRequest request, ServerSipResponse response) {
            return delegate.flatMap((handler -> handler.handle(request, response)));
        }
    }
}

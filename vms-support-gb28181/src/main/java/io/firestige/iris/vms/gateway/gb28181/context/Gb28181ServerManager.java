package io.firestige.iris.vms.gateway.gb28181.context;

import io.firestige.iris.vms.gateway.gb28181.server.Gb28181Server;
import io.firestige.iris.vms.gateway.gb28181.server.ReactiveGb28181ServerFactory;
import io.firestige.iris.vms.gateway.sip.ServerSipRequest;
import io.firestige.iris.vms.gateway.sip.ServerSipResponse;
import io.firestige.iris.vms.gateway.sip.SipHandler;
import org.springframework.boot.web.server.GracefulShutdownCallback;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

class Gb28181ServerManager {
    private final ReactiveGb28181ServerApplicationContext ctx;
    private final DelayedInitializationSipHandler handler;
    private final Gb28181Server server;

    public Gb28181ServerManager(ReactiveGb28181ServerApplicationContext ctx, ReactiveGb28181ServerFactory factory,
                                Supplier<SipHandler> handlerSupplier, boolean lazyInit) {
        this.ctx = ctx;
        Assert.notNull(factory, "Factory must not be null");
        this.handler = new DelayedInitializationSipHandler(handlerSupplier, lazyInit);
        this.server = factory.getGb28181Server(this.handler);
    }

    void start() {
        this.handler.initializeHandler();
        this.server.start();
        this.ctx.publishEvent(new ReactiveGb28181ServerInitializedEvent(this.server, this.ctx));
    }

    void shutDownGracefully(GracefulShutdownCallback callback) {
        this.server.shutDownGracefully(callback);
    }

    void stop() {
        this.server.stop();
    }

    Gb28181Server getServer() {
        return this.server;
    }

    SipHandler getHandler() {
        return this.handler;
    }

    static final class DelayedInitializationSipHandler implements SipHandler {
        private final Supplier<SipHandler> handlerSupplier;
        private final boolean lazyInit;
        private volatile SipHandler delegate = this::handleUninitialized;

        public DelayedInitializationSipHandler(Supplier<SipHandler> handlerSupplier, boolean lazyInit) {
            this.handlerSupplier = handlerSupplier;
            this.lazyInit = lazyInit;
        }

        private Mono<Void> handleUninitialized(ServerSipRequest request, ServerSipResponse response) {
            throw new IllegalStateException("The SipHandler has not yet been initialized");
        }

        @Override
        public Mono<Void> handle(ServerSipRequest request, ServerSipResponse response) {
            return this.delegate.handle(request, response);
        }

        void initializeHandler() {
            this.delegate = this.lazyInit ? new LazySipHandler(this.handlerSupplier) : this.handlerSupplier.get();
        }

        SipHandler getHandler() {
            return this.delegate;
        }
    }

    private static final class LazySipHandler implements SipHandler {
        private final Mono<SipHandler> delegate;
        private LazySipHandler(Supplier<SipHandler> handlerSupplier) {
            this.delegate = Mono.fromSupplier(handlerSupplier);
        }

        @Override
        public Mono<Void> handle(ServerSipRequest request, ServerSipResponse response) {
            return this.delegate.flatMap(handler -> handler.handle(request, response));
        }
    }
}

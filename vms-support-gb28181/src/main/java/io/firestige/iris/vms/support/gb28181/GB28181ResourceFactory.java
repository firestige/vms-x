package io.firestige.iris.vms.support.gb28181;

import reactor.netty.http.HttpResources;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * GB28181ResourceFactory
 *
 * @author firestige
 * @createAt 2023/6/13
 **/
public class GB28181ResourceFactory implements InitializingBean, DisposableBean {
    private boolean useGlobalResource = true;
    private Consumer<HttpResources> globalResourceConsumer;
    private Supplier<ConnectionProvider> connectionProviderSupplier = () -> ConnectionProvider.create("GB28181");
    private ConnectionProvider connectionProvider;
    private Supplier<LoopResources> loopResourcesSupplier = () -> LoopResources.create("GB28181-sip");
    private LoopResources loopResources;
    private boolean manageConnectionProvider = false;

    private boolean manageLoopResources = false;

    private Duration shutdownQuietPeriod = Duration.ofSeconds(LoopResources.DEFAULT_SHUTDOWN_QUIET_PERIOD);

    private Duration shutdownTimeout = Duration.ofSeconds(LoopResources.DEFAULT_SHUTDOWN_TIMEOUT);

    public boolean isUseGlobalResource() {
        return useGlobalResource;
    }

    public void setUseGlobalResource(boolean useGlobalResource) {
        this.useGlobalResource = useGlobalResource;
    }

    public void addGlobalResourceConsumer(Consumer<HttpResources> globalResourceConsumer) {
        this.useGlobalResource = true;
        this.globalResourceConsumer = Optional.ofNullable(this.globalResourceConsumer)
                .map(consumer -> consumer.andThen(globalResourceConsumer))
                .orElse(globalResourceConsumer);
    }

    public void setConnectionProviderSupplier(Supplier<ConnectionProvider> connectionProviderSupplier) {
        this.connectionProviderSupplier = connectionProviderSupplier;
    }

    public void setConnectionProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public void setLoopResourcesSupplier(Supplier<LoopResources> loopResourcesSupplier) {
        this.loopResourcesSupplier = loopResourcesSupplier;
    }

    public void setLoopResources(LoopResources loopResources) {
        this.loopResources = loopResources;
    }

    public LoopResources getLoopResources() {
        return loopResources;
    }

    public void setShutdownQuietPeriod(Duration shutdownQuietPeriod) {
        this.shutdownQuietPeriod = shutdownQuietPeriod;
    }

    public void setShutdownTimeout(Duration shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }


    @Override
    public void destroy() throws Exception {
        if (this.useGlobalResource) {
            HttpResources.disposeLoopsAndConnectionsLater(this.shutdownQuietPeriod, this.shutdownTimeout).block();
        }
        else {
            try {
                ConnectionProvider provider = this.connectionProvider;
                if (provider != null && this.manageConnectionProvider) {
                    provider.disposeLater().block();
                }
            }
            catch (Throwable ex) {
                // ignore
            }

            try {
                LoopResources resources = this.loopResources;
                if (resources != null && this.manageLoopResources) {
                    resources.disposeLater(this.shutdownQuietPeriod, this.shutdownTimeout).block();
                }
            }
            catch (Throwable ex) {
                // ignore
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.useGlobalResource) {
            Assert.isTrue(this.loopResources == null && this.connectionProvider == null,
                    "'useGlobalResources' is mutually exclusive with explicitly configured resources");
            HttpResources httpResources = HttpResources.get();
            if (this.globalResourceConsumer != null) {
                this.globalResourceConsumer.accept(httpResources);
            }
            this.connectionProvider = httpResources;
            this.loopResources = httpResources;
        }
        else {
            if (this.loopResources == null) {
                this.manageLoopResources = true;
                this.loopResources = this.loopResourcesSupplier.get();
            }
            if (this.connectionProvider == null) {
                this.manageConnectionProvider = true;
                this.connectionProvider = this.connectionProviderSupplier.get();
            }
        }
    }
}

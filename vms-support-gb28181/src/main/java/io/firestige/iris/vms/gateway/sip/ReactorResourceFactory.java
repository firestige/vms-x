package io.firestige.iris.vms.gateway.sip;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReactorResourceFactory implements InitializingBean, DisposableBean {
    private boolean useGlobalResources = true;
    @Nullable
    private Consumer<LoopResources> globalResourcesConsumer;
    private Supplier<ConnectionProvider> connectionProviderSupplier =
            () -> ConnectionProvider.create("vms-gb28181", 500);
    private ConnectionProvider connectionProvider;
    private Supplier<LoopResources> loopResourcesSupplier = () -> LoopResources.create("vms-gb28181-sip");
    @Nullable
    private LoopResources loopResources;
    private boolean manageConnectionProvider = false;
    private boolean manageLoopResources = false;
    private Duration shutdownQuietPeriod = Duration.ofSeconds(LoopResources.DEFAULT_SHUTDOWN_QUIET_PERIOD);
    private Duration shutdownTimeout = Duration.ofSeconds(LoopResources.DEFAULT_SHUTDOWN_TIMEOUT);

    public void setUseGlobalResources(boolean useGlobalResources) {
        this.useGlobalResources = useGlobalResources;
    }

    public boolean isUseGlobalResources() {
        return useGlobalResources;
    }

    public void addGlobalResourceConsumer(Consumer<LoopResources> consumer) {
        this.useGlobalResources = true;
        this.globalResourcesConsumer = Objects.isNull(this.globalResourcesConsumer) ? consumer :
                this.globalResourcesConsumer.andThen(consumer);
    }

    public void setConnectionProviderSupplier(Supplier<ConnectionProvider> connectionProviderSupplier) {
        this.connectionProviderSupplier = connectionProviderSupplier;
    }

    public void setConnectionProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void setLoopResourcesSupplier(Supplier<LoopResources> loopResourcesSupplier) {
        this.loopResourcesSupplier = loopResourcesSupplier;
    }

    public void setLoopResources(LoopResources loopResources) {
        this.loopResources = loopResources;
    }

    public LoopResources getLoopResources() {
        Assert.state(Objects.nonNull(this.loopResources), "LoopResources not initialized yet");
        return loopResources;
    }

    public void setShutdownQuietPeriod(Duration shutdownQuietPeriod) {
        Assert.notNull(shutdownQuietPeriod, "shutdownQuietPeriod should not be null");
        this.shutdownQuietPeriod = shutdownQuietPeriod;
    }

    public void setShutdownTimeout(Duration shutdownTimeout) {
        Assert.notNull(shutdownTimeout, "shutdownTimeout should not be null");
        this.shutdownTimeout = shutdownTimeout;
    }

    @Override
    public void destroy() throws Exception {
        if (this.useGlobalResources) {
            // todo release resources
        } else {
            try {
                ConnectionProvider provider = this.connectionProvider;
                if (Objects.nonNull(provider) && this.manageConnectionProvider) {
                    provider.disposeLater().block();
                }
            } catch (Throwable cause) {
                // ignore
            }
            try {
                LoopResources resources = this.loopResources;
                if (Objects.nonNull(resources) && this.manageLoopResources) {
                    resources.disposeLater(this.shutdownQuietPeriod, this.shutdownTimeout).block();
                }
            } catch (Throwable cause) {
                // ignore
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.useGlobalResources) {
            Assert.isTrue(Objects.isNull(this.loopResources) && Objects.isNull(this.connectionProvider),
                    "'useGlobalResources' is mutually exclusive with explicitly configured resources");
//            SipResources sipResources = SipResources.get();
//            if (Objects.nonNull(this.globalResourcesConsumer)) {
//                this.globalResourcesConsumer.accept(sipResources);
//            }
//            this.connectionProvider = sipResources;
//            this.loopResources = sipResources;
        } else {
            if (Objects.isNull(this.loopResources)) {
                this.manageLoopResources = true;
                this.loopResources = this.loopResourcesSupplier.get();
            }
            if (Objects.isNull(this.connectionProvider)) {
                this.manageConnectionProvider = true;
                this.connectionProvider = this.connectionProviderSupplier.get();
            }
        }
    }
}

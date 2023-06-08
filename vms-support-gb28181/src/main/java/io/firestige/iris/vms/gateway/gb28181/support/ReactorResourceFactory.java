package io.firestige.iris.vms.gateway.gb28181.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import reactor.netty.http.HttpResources;
import reactor.netty.resources.LoopResources;

import java.time.Duration;
import java.util.function.Supplier;

public class ReactorResourceFactory implements InitializingBean, DisposableBean {
    private boolean useGlobalResources = true;
    private boolean manageLoopResources = false;
    private Duration shutdownQuietPeriod = Duration.ofSeconds(LoopResources.DEFAULT_SHUTDOWN_QUIET_PERIOD);

    private Duration shutdownTimeout = Duration.ofSeconds(LoopResources.DEFAULT_SHUTDOWN_TIMEOUT);

    private Supplier<LoopResources> loopResourcesSupplier = () -> LoopResources.create("Gb28181-sip");
    @Nullable
    private LoopResources loopResources;

    public boolean isUseGlobalResources() {
        return useGlobalResources;
    }

    public void setUseGlobalResources(boolean useGlobalResources) {
        this.useGlobalResources = useGlobalResources;
    }

    public void setLoopResourcesSupplier(Supplier<LoopResources> loopResourcesSupplier) {
        this.loopResourcesSupplier = loopResourcesSupplier;
    }

    public void setLoopResources(@Nullable LoopResources loopResources) {
        this.loopResources = loopResources;
    }

    public LoopResources getLoopResources() {
        Assert.state(this.loopResources != null, "LoopResources not initialized yet");
        return this.loopResources;
    }

    public void setShutdownQuietPeriod(Duration shutdownQuietPeriod) {
        this.shutdownQuietPeriod = shutdownQuietPeriod;
    }

    public void setShutdownTimeout(Duration shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.useGlobalResources) {
            Assert.isTrue(this.loopResources == null,
                    "'useGlobalResources' is mutually exclusive with explicitly configured resources");
            this.loopResources = HttpResources.get();
        } else {
            if (this.loopResources == null) {
                this.manageLoopResources = true;
                this.loopResources = this.loopResourcesSupplier.get();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        if (this.useGlobalResources) {
            HttpResources.disposeLoopsAndConnectionsLater(this.shutdownQuietPeriod, this.shutdownTimeout).block();
        } else {
            try {
                LoopResources resources = loopResources;
                if (resources != null && this.manageLoopResources) {
                    resources.disposeLater(shutdownQuietPeriod, shutdownTimeout).block();
                }
            } catch (Throwable ignore) {
                // ignore
            }
        }
    }
}

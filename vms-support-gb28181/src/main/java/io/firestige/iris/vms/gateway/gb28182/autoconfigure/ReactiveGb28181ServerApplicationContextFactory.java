package io.firestige.iris.vms.gateway.gb28182.autoconfigure;

import io.firestige.iris.vms.gateway.gb28182.context.ReactiveGb28181ServerApplicationContext;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

class ReactiveGb28181ServerApplicationContextFactory implements ApplicationContextFactory {
    @Override
    public Class<? extends ConfigurableEnvironment> getEnvironmentType(WebApplicationType webApplicationType) {
        return getOrNull(webApplicationType, ApplicationReactiveGb28181Environment.class);
    }

    @Override
    public ConfigurableEnvironment createEnvironment(WebApplicationType webApplicationType) {
        return getOrNull(webApplicationType, new ApplicationReactiveGb28181Environment());
    }

    @Override
    public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {
        return getOrNull(webApplicationType, new ReactiveGb28181ServerApplicationContext());
    }

    private <T> T getOrNull(WebApplicationType webApplicationType, T t) {
        return webApplicationType == WebApplicationType.REACTIVE ? t : null;
    }
}

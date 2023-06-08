package io.firestige.iris.vms.gateway.gb28181.context;

import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

class SipServerApplicationContextFactory implements ApplicationContextFactory {
    @Override
    public Class<? extends ConfigurableEnvironment> getEnvironmentType(WebApplicationType webApplicationType) {
        return ApplicationGb28181Environment.class;
    }

    @Override
    public ConfigurableEnvironment createEnvironment(WebApplicationType webApplicationType) {
        return new ApplicationGb28181Environment();
    }

    @Override
    public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {
        return new Gb28181ServerApplicationContext();
    }
}

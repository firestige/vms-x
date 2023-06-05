package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.web.reactive.context.StandardReactiveWebEnvironment;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.MutablePropertySources;

public class ApplicationReactiveGb28181Environment extends StandardReactiveWebEnvironment {
    @Override
    protected String doGetActiveProfilesProperty() {
        return null;
    }

    @Override
    protected String doGetDefaultProfilesProperty() {
        return null;
    }

    @Override
    protected ConfigurablePropertyResolver createPropertyResolver(MutablePropertySources propertySources) {
        return ConfigurationPropertySources.createPropertyResolver(propertySources);
    }
}

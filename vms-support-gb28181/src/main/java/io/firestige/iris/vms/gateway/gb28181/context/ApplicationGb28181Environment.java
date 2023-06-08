package io.firestige.iris.vms.gateway.gb28181.context;

import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

class ApplicationGb28181Environment extends StandardEnvironment implements ConfigurableEnvironment {
    public ApplicationGb28181Environment() {
        super();
    }

    protected ApplicationGb28181Environment(MutablePropertySources propertySources) {
        super(propertySources);
    }

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

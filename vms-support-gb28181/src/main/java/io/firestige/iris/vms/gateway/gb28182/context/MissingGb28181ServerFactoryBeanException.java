package io.firestige.iris.vms.gateway.gb28182.context;

import io.firestige.iris.vms.gateway.gb28182.server.ReactiveGb28181ServerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.WebApplicationType;

public class MissingGb28181ServerFactoryBeanException extends NoSuchBeanDefinitionException {
    private final WebApplicationType webApplicationType;
    public MissingGb28181ServerFactoryBeanException(
            Class<? extends ReactiveGb28181ServerApplicationContext> ctxClass,
            Class<ReactiveGb28181ServerFactory> factoryClass,
            WebApplicationType webApplicationType) {
        super(factoryClass, String.format("Unable to start %s due to missing %s bean", ctxClass.getSimpleName(),
                factoryClass.getSimpleName()));
        this.webApplicationType = webApplicationType;
    }

    public WebApplicationType getWebApplicationType() {
        return this.webApplicationType;
    }
}

package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import io.firestige.iris.vms.gateway.gb28181.config.DelegatingGb28181Configuration;
import io.firestige.iris.vms.gateway.gb28181.config.Gb28181ConfigurationSupport;
import io.firestige.iris.vms.gateway.gb28181.config.Gb28181Configurer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@AutoConfiguration(after = {SipServerFactoryAutoConfiguration.class})
@ConditionalOnClass(Gb28181Configurer.class)
@ConditionalOnMissingBean({Gb28181ConfigurationSupport.class})
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
public class Gb28181AutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties({Gb28181Properties.class})
    @Import({EnableGb28181Configuration.class})
    @Order(0)
    public static class Gb28181Config implements Gb28181Configurer {

    }
    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties({Gb28181Properties.class, SipServerProperties.class})
    public static class EnableGb28181Configuration extends DelegatingGb28181Configuration {

    }
}

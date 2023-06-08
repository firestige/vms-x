package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import io.firestige.iris.vms.gateway.gb28181.support.ReactorResourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.reactor.netty.ReactorNettyProperties;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

public final class ReactorNettyConfigurations {
    private ReactorNettyConfigurations() {}

    public static class ReactorResourceFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        ReactorResourceFactory reactorResourceFactory(ReactorNettyProperties properties) {
            ReactorResourceFactory factory = new ReactorResourceFactory();
            Optional.ofNullable(properties.getShutdownQuietPeriod()).ifPresent(factory::setShutdownQuietPeriod);
            return factory;
        }
    }
}

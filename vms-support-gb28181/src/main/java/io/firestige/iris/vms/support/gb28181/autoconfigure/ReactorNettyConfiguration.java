package io.firestige.iris.vms.support.gb28181.autoconfigure;

import io.firestige.iris.vms.support.gb28181.GB28181ResourceFactory;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.reactor.netty.ReactorNettyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ReactorNettyConfiguration
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ReactorNettyProperties.class)
public class ReactorNettyConfiguration {
    @Bean
    @ConditionalOnMissingBean
    GB28181ResourceFactory gb28181ResourceFactory(ReactorNettyProperties properties) {
        GB28181ResourceFactory factory = new GB28181ResourceFactory();
        if (properties.getShutdownQuietPeriod() != null) {
            factory.setShutdownQuietPeriod(properties.getShutdownQuietPeriod());
        }
        return factory;
    }
}

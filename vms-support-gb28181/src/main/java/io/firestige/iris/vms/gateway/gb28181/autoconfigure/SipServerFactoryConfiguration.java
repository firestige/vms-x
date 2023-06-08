package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import io.firestige.iris.vms.gateway.gb28181.server.SipHandler;
import io.firestige.iris.vms.gateway.gb28181.server.embedded.NettyServerCustomizer;
import io.firestige.iris.vms.gateway.gb28181.server.embedded.NettySipServerFactory;
import io.firestige.iris.vms.gateway.gb28181.support.ReactorResourceFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public final class SipServerFactoryConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(NettySipServerFactory.class)
    @ConditionalOnClass({SipHandler.class})
    @Import({ReactorNettyConfigurations.ReactorResourceFactoryConfiguration.class})
    static class EmbeddedNetty {
        @Bean
        NettySipServerFactory nettySipServerFactory(
                ReactorResourceFactory resourceFactory,
                ObjectProvider<NettyServerCustomizer> customizers) {
            NettySipServerFactory serverFactory = new NettySipServerFactory();
            serverFactory.setResourceFactory(resourceFactory);
            serverFactory.getCustomizers().addAll(customizers.orderedStream().toList());
            return serverFactory;
        }
    }
}

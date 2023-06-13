package io.firestige.iris.vms.support.gb28181.autoconfigure;

import io.firestige.iris.vms.support.gb28181.GB28181ResourceFactory;
import io.firestige.iris.vms.support.gb28181.server.SipServer;
import io.firestige.iris.vms.support.gb28181.server.SipServerFactory;
import io.firestige.iris.vms.support.gb28181.server.embedded.NettyServerCustomizer;
import io.firestige.iris.vms.support.gb28181.server.embedded.NettySipServerFactory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * SipServerFactoryConfiguration
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(SipServerFactory.class)
@ConditionalOnClass({SipServer.class})
@Import(ReactorNettyConfiguration.class)
class SipServerFactoryConfiguration {
    @Bean
    NettySipServerFactory nettySipServerFactory(GB28181ResourceFactory resourceFactory,
                                                ObjectProvider<NettyServerCustomizer> serverCustomizers) {
        NettySipServerFactory factory = new NettySipServerFactory();
        factory.setResourceFactory(resourceFactory);
        factory.getServerCustomizers().addAll(serverCustomizers.orderedStream().toList());
        return factory;
    }
}

package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import io.firestige.iris.vms.gateway.gb28181.server.NettyReactiveGb28181ServerFactory;
import io.firestige.iris.vms.gateway.gb28181.server.NettyServerCustomizer;
import io.firestige.iris.vms.gateway.gb28181.server.ReactiveGb28181ServerFactory;
import io.firestige.iris.vms.gateway.sip.SipServer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

abstract class ReactiveGb28181ServerFactoryConfiguration {
    private ReactiveGb28181ServerFactoryConfiguration() {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(ReactiveGb28181ServerFactory.class)
    @ConditionalOnClass({ SipServer.class })
    static class EmbeddedNetty {
        @Bean
        NettyReactiveGb28181ServerFactory nettyReactiveGb28181ServerFactory(ObjectProvider<NettyServerCustomizer> serverCustomizers) {
            NettyReactiveGb28181ServerFactory serverFactory = new NettyReactiveGb28181ServerFactory();
            serverFactory.getServerCustomizers().addAll(serverCustomizers.orderedStream().toList());
            return serverFactory;
        }
    }
}

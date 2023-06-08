package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import io.firestige.iris.vms.gateway.gb28181.server.SipServerFactory;
import io.firestige.iris.vms.gateway.gb28181.server.SipServerFactoryCustomizer;
import io.firestige.iris.vms.gateway.gb28181.server.SipServerFactoryCustomizerBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ObjectUtils;

@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration
//@ConditionalOnClass(ReactiveSipInputMessage.class)
@EnableConfigurationProperties({
        SipServerFactoryAutoConfiguration.BeanPostProcessorsRegister.class,
        SipServerFactoryConfiguration.EmbeddedNetty.class
})
public class SipServerFactoryAutoConfiguration {

    @Bean
    public SipServerFactoryCustomizer<SipServerFactory> sipServerFactorySipServerFactoryCustomizer(SipServerProperties properties) {
        return new ReactiveSipServerFactoryCustomizer(properties);
    }

    public static class BeanPostProcessorsRegister implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
        private ConfigurableListableBeanFactory beanFactory;
        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            if (beanFactory instanceof ConfigurableListableBeanFactory listableBeanFactory) {
                this.beanFactory = listableBeanFactory;
            }
        }

        @Override
        public void registerBeanDefinitions(
                AnnotationMetadata importingClassMetadata,
                BeanDefinitionRegistry registry) {
            if (this.beanFactory == null) {
                return;
            }
            if (ObjectUtils.isEmpty(
                    this.beanFactory.getBeanNamesForType(SipServerFactoryCustomizerBeanPostProcessor.class, true, false))) {
                RootBeanDefinition beanDefinition =
                        new RootBeanDefinition(SipServerFactoryCustomizerBeanPostProcessor.class);
                beanDefinition.setSynthetic(true);
                registry.registerBeanDefinition("webServerFactoryCustomizerBeanPostProcessor", beanDefinition);
            }
        }
    }
}

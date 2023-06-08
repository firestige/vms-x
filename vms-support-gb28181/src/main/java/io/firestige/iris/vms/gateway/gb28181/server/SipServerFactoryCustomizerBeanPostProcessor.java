package io.firestige.iris.vms.gateway.gb28181.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.util.LambdaSafe;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SipServerFactoryCustomizerBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private ListableBeanFactory beanFactory;
    private List<SipServerFactoryCustomizer<?>> customizers;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Assert.isInstanceOf(ListableBeanFactory.class, beanFactory,
                "SipServerFactoryCustomizerBeanPostProcessor can only be used with a ListableBeanFactory");
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof SipServerFactory sipServerFactory) {
            postProcessBeforeInitialization(sipServerFactory);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void postProcessBeforeInitialization(SipServerFactory factory) {
        LambdaSafe.callbacks(SipServerFactoryCustomizer.class, getCustomizers(), factory)
                .withLogger(SipServerFactoryCustomizerBeanPostProcessor.class)
                .invoke((customizer) -> customizer.customize(factory));
    }

    private Collection<SipServerFactoryCustomizer<?>> getCustomizers() {
        if (this.customizers == null) {
            // Look up does not include the parent context
            this.customizers = new ArrayList<>(getSipServerFactoryCustomizerBeans());
            this.customizers.sort(AnnotationAwareOrderComparator.INSTANCE);
            this.customizers = Collections.unmodifiableList(this.customizers);
        }
        return this.customizers;
    }

    private Collection<SipServerFactoryCustomizer<?>> getSipServerFactoryCustomizerBeans() {
        return (Collection) this.beanFactory.getBeansOfType(SipServerFactoryCustomizer.class, false, false).values();
    }
}

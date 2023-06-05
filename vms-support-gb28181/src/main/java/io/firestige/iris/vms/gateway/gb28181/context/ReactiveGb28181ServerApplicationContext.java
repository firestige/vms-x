package io.firestige.iris.vms.gateway.gb28181.context;

import io.firestige.iris.vms.gateway.gb28181.server.Gb28181Server;
import io.firestige.iris.vms.gateway.gb28181.server.ReactiveGb28181ServerFactory;
import io.firestige.iris.vms.gateway.sip.SipHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.metrics.StartupStep;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * 通用GB28181上下文，通过调用ReactiveGb28181ServerFactory完成自动创建服务端
 * {@link ReactiveGb28181ServerFactory}
 */
public class ReactiveGb28181ServerApplicationContext extends GenericApplicationContext implements Gb28181ApplicationContext {
    private String namespace;
    private volatile Gb28181ServerManager serverManager;

    /**
     * 构造器
     */
    public ReactiveGb28181ServerApplicationContext() {
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        try {
            super.refresh();
        } catch (RuntimeException ex) {
            Optional.ofNullable(this.serverManager)
                    .map(Gb28181ServerManager::getServer)
                    .ifPresent(Gb28181Server::stop);
            throw ex;
        }
    }

    @Override
    protected void onRefresh() throws BeansException {
        super.onRefresh();
        try {
            createGb28181Server();
        } catch (Throwable cause) {
            throw new ApplicationContextException("Unable to start reactive gb28181 server", cause);
        }
    }

    private void createGb28181Server() {
        Gb28181ServerManager manager = this.serverManager;
        if (Objects.isNull(manager)) {
            StartupStep createServer = getApplicationStartup().start("gb28181server.create");
            String factoryBeanName = getGb28181ServerFactoryBeanName();
            ReactiveGb28181ServerFactory factory = getServerFactory(factoryBeanName);
            createServer.tag("factory", factory.getClass().toGenericString());
            boolean lazyInit = getBeanFactory().getBeanDefinition(factoryBeanName).isLazyInit();
            this.serverManager = new Gb28181ServerManager(this, factory, this::getSipHandler, lazyInit);
            getBeanFactory().registerSingleton("gb28181ServerGracefulShutdown",
                    new Gb28181ServerGracefulShutdownLifecycle(this.serverManager.getServer()));
            getBeanFactory().registerSingleton("gb28181ServerStartStop",
                    new Gb28181ServerStartStopLifecycle(this.serverManager));
            createServer.end();
        }
        initPropertySources();
    }

    protected String getGb28181ServerFactoryBeanName() {
        String[] beanNames = getBeanFactory().getBeanNamesForType(ReactiveGb28181ServerFactory.class);
        if (beanNames.length == 0) {
            throw new MissingGb28181ServerFactoryBeanException(getClass(), ReactiveGb28181ServerFactory.class,
                    WebApplicationType.REACTIVE);
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException("Unable to start ReactiveGb28181ApplicationContext due to multiple "
                    + "ReactiveGb28181ServerFactory beans : " + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
        return beanNames[0];
    }

    protected ReactiveGb28181ServerFactory getServerFactory(String factoryBeanName) {
        return getBeanFactory().getBean(factoryBeanName, ReactiveGb28181ServerFactory.class);
    }

    protected SipHandler getSipHandler() {
        String[] beanNames = getBeanFactory().getBeanNamesForType(SipHandler.class);
        if (beanNames.length == 0) {
            throw new ApplicationContextException(
                    "Unable to start ReactiveGb28181ApplicationContext due to missing SipHandler bean.");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException(
                    "Unable to start ReactiveGb28181ApplicationContext due to multiple SipHandler beans : "
                            + StringUtils.arrayToCommaDelimitedString(beanNames));
        }
        return getBeanFactory().getBean(beanNames[0], SipHandler.class);
    }

    @Override
    protected void doClose() {
        if (isActive()) {
            AvailabilityChangeEvent.publish(this, ReadinessState.REFUSING_TRAFFIC);
        }
        super.doClose();
    }

    @Override
    public Gb28181Server getGb28181Server() {
        Gb28181ServerManager serverManager = this.serverManager;
        return Objects.isNull(serverManager) ? null : serverManager.getServer();
    }

    @Override
    public String getServerNamespace() {
        return namespace;
    }

    @Override
    public void setServerNamespace(String namespace) {
        this.namespace = namespace;
    }
}

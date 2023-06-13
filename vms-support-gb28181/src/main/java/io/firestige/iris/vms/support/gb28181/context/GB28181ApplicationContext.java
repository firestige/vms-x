package io.firestige.iris.vms.support.gb28181.context;

import io.firestige.iris.core.context.GenericVmsApplicationContext;
import io.firestige.iris.vms.support.gb28181.server.SipHandler;
import io.firestige.iris.vms.support.gb28181.server.SipServer;
import io.firestige.iris.vms.support.gb28181.server.SipServerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.metrics.StartupStep;

import java.util.Optional;

/**
 * GB28181ApplicationContext
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public class GB28181ApplicationContext extends GenericVmsApplicationContext implements ConfigurableSipServerApplicationContext {
    private volatile GB28181ServerManager serverManager;
    private String serverNamespace;

    /**
     * 构造器
     */
    public GB28181ApplicationContext() {
    }

    /**
     * 使用传入的Bean工厂创建实例
     *
     * @param factory 用于创建实例的Bean工厂
     */
    public GB28181ApplicationContext(DefaultListableBeanFactory factory) {
        super(factory);
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        try {
            super.refresh();
        } catch (RuntimeException cause) {
            Optional.ofNullable(this.serverManager).map(GB28181ServerManager::getServer).ifPresent(SipServer::stop);
            throw cause;
        }
    }

    @Override
    protected void onRefresh() throws BeansException {
        super.onRefresh();
        try {
            createSipServer();
        } catch (Throwable cause) {
            throw new ApplicationContextException("Unable to start sip server", cause);
        }
    }

    private void createSipServer() {
        GB28181ServerManager manager = this.serverManager;
        if (manager == null) {
            StartupStep createSipServer = getApplicationStartup().start("Iris.SipServer.create");
            String factoryBeanName = getSipServerFactoryBeanName();
            SipServerFactory factory = getSipServerFactory(factoryBeanName);
            createSipServer.tag("factory", factoryBeanName);
            boolean lazyInit = getBeanFactory().getBeanDefinition(factoryBeanName).isLazyInit();
            this.serverManager = new GB28181ServerManager(this, factory, this::getHandler, lazyInit);
            getBeanFactory().registerSingleton("sipServerGracefulShutdown",
                    new GB28181ServerGracefulShutdownLifecycle(this.serverManager.getServer()));
            getBeanFactory().registerSingleton("sipServerStartStop",
                    new GB28181ServerStartStopLifecycle(this.serverManager));
            createSipServer.end();
        }
        initPropertySources();
    }

    private String getSipServerFactoryBeanName() {
        String[] beanNames = getBeanFactory().getBeanNamesForType(SipServerFactory.class);
        if (beanNames.length == 0) {
            throw new ApplicationContextException("No SipServer factory bean defined");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException("Multiple SipServer factory beans defined");
        }
        return beanNames[0];
    }

    private SipServerFactory getSipServerFactory(String factoryBeanName) {
        return getBeanFactory().getBean(factoryBeanName, SipServerFactory.class);
    }

    private SipHandler getHandler() {
        String[] beanNames = getBeanFactory().getBeanNamesForType(SipHandler.class);
        if (beanNames.length == 0) {
            throw new ApplicationContextException("No SipHandler bean defined");
        }
        if (beanNames.length > 1) {
            throw new ApplicationContextException("Multiple SipHandler beans defined");
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
    public SipServer getSipServer() {
        GB28181ServerManager manager = this.serverManager;
        return manager != null ? manager.getServer() : null;
    }

    @Override
    public String getServerNamespace() {
        return this.serverNamespace;
    }

    @Override
    public void setServerNamespace(String serverNamespace) {
        this.serverNamespace = serverNamespace;
    }
}

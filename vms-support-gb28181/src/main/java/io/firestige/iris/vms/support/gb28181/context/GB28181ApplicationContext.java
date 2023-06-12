package io.firestige.iris.vms.support.gb28181.context;

import io.firestige.iris.core.context.GenericVmsApplicationContext;
import io.firestige.iris.vms.support.sip.server.SipServer;
import io.firestige.iris.vms.support.sip.context.ConfigurableSipServerApplicationContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextException;

import java.util.Optional;

/**
 * GB28181ApplicationContext
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public class GB28181ApplicationContext extends GenericVmsApplicationContext implements ConfigurableSipServerApplicationContext {
    private volatile SipServerManager serverManager;
    private String serverNamespace;

    public GB28181ApplicationContext() {
    }

    public GB28181ApplicationContext(DefaultListableBeanFactory factory) {
        super(factory);
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        try {
            super.refresh();
        } catch (RuntimeException cause) {
            Optional.ofNullable(this.serverManager).map(SipServerManager::getServer).ifPresent(SipServer::stop);
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
    }
}

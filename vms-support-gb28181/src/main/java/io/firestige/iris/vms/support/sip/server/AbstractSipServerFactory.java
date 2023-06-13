package io.firestige.iris.vms.support.sip.server;

import io.firestige.iris.core.server.AbstractConfigurableVmsServerFactory;

/**
 * AbstractSipServerFactory
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public abstract class AbstractSipServerFactory extends AbstractConfigurableVmsServerFactory
        implements ConfigurableSipServerFactory {
    public AbstractSipServerFactory() {
    }

    public AbstractSipServerFactory(int port) {
        super(port);
    }
}

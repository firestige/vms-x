package io.firestige.iris.vms.gateway.gb28181.context;

import org.springframework.context.ConfigurableApplicationContext;

public interface ConfigurableSipServerApplicationContext extends ConfigurableApplicationContext, SipServerApplicationContext {
    void setServerNamespace(String serverNamespace);
}

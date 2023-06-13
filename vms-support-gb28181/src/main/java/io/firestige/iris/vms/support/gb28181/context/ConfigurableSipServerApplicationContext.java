package io.firestige.iris.vms.support.gb28181.context;

/**
 * ConfigurableSuoServerApplication
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public interface ConfigurableSipServerApplicationContext extends SipServerApplicationContext {
    void setServerNamespace(String serverNamespace);
}

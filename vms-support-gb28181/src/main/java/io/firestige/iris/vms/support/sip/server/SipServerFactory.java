package io.firestige.iris.vms.support.sip.server;

import io.firestige.iris.core.server.VmsServerFactory;

/**
 * SipServerFactory
 *
 * @author firestige
 * @createAt 2023/6/11
 **/
@FunctionalInterface
public interface SipServerFactory extends VmsServerFactory {
    SipServer getSipServer(SipHandler handler);
}

package xyz.firestige.vmsx.support.gb28181.server;

import xyz.firestige.vmsx.core.server.VmsServerFactory;

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

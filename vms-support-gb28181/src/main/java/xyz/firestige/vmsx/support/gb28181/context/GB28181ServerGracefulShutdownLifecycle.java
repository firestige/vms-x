package xyz.firestige.vmsx.support.gb28181.context;

import xyz.firestige.vmsx.core.context.GenericVmsServerGracefulShutdownLifecycle;
import xyz.firestige.vmsx.support.gb28181.server.SipServer;

/**
 * SipServerGracefulShutdownLifecycle
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/13
 **/
final class GB28181ServerGracefulShutdownLifecycle extends GenericVmsServerGracefulShutdownLifecycle {
    public GB28181ServerGracefulShutdownLifecycle(SipServer server) {
        super(server);
    }
}

package io.firestige.iris.vms.support.gb28181.context;

import io.firestige.iris.core.context.GenericVmsServerGracefulShutdownLifecycle;
import io.firestige.iris.vms.support.gb28181.server.SipServer;

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

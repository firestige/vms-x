package io.firestige.iris.vms.support.sip.server.embedded;

import io.firestige.iris.core.server.ServerException;
import io.firestige.iris.core.server.ShutdownStrategy;
import io.firestige.iris.vms.support.sip.server.SipServer;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;

/**
 * NettySipServer
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public class NettySipServer implements SipServer {
    public NettySipServer(HttpServer httpServer, SipHandlerAdapter handlerAdapter, Duration lifecycleTimeout, ShutdownStrategy shutdown) {

    }

    @Override
    public void start() throws ServerException {

    }

    @Override
    public void stop() throws ServerException {

    }

    @Override
    public int getPort() {
        return 0;
    }
}

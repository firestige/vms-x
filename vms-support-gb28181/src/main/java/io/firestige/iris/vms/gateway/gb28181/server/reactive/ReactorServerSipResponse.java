package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.sip.ServerSipResponse;
import io.firestige.iris.vms.gateway.sip.SipServerResponse;
import org.springframework.core.io.buffer.NettyDataBufferFactory;

public class ReactorServerSipResponse extends AbstractServerSipResponse implements ZeroCopySipOutputMessage {
    public ReactorServerSipResponse(SipServerResponse reactorResponse, NettyDataBufferFactory bufferFactory) {
    }
}

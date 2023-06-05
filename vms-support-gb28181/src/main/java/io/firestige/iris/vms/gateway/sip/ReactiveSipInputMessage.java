package io.firestige.iris.vms.gateway.sip;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public interface ReactiveSipInputMessage extends SipMessage {
    Flux<DataBuffer> getBody();
}

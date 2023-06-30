package xyz.firestige.vmsx.support.gb28181;

import reactor.core.publisher.Flux;

import org.springframework.core.io.buffer.DataBuffer;

/**
 * SipInputMessage
 *
 * @author firestige
 * @createAt 2023/6/14
 **/
public interface SipInputMessage extends SipMessage {
    Flux<DataBuffer> getBody();
}

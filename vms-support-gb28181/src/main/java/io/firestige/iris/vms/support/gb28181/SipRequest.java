package io.firestige.iris.vms.support.gb28181;

import java.net.URI;

/**
 * SipRequest
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
public interface SipRequest extends SipMessage {
    SipMethod getMethod();
    URI getUri();
}

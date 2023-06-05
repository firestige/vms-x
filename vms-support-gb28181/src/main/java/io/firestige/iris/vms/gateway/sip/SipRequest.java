package io.firestige.iris.vms.gateway.sip;

import java.net.URI;

public interface SipRequest extends SipMessage {
    SipMethod getMethod();
    URI getURI();
}

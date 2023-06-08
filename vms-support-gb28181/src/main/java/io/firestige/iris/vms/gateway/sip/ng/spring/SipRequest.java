package io.firestige.iris.vms.gateway.sip.ng.spring;

import io.firestige.iris.vms.gateway.gb28182.SipMessage;

import java.net.URI;

public interface SipRequest extends SipMessage {
    SipMethod getMethod();
    URI getURI();
}

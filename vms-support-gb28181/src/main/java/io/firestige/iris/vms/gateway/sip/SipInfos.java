package io.firestige.iris.vms.gateway.sip;

import io.firestige.iris.vms.gateway.sip.ng.spring.SipMethod;

public interface SipInfos {
    String requestId();
    SipMethod method();
    String uri();
    SipVersion version();
}

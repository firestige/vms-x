package io.firestige.iris.vms.gateway.sip;

public interface SipInfos {
    String requestId();
    SipMethod method();
    String uri();
    SipVersion version();
}

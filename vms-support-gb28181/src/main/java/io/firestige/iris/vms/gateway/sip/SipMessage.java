package io.firestige.iris.vms.gateway.sip;

import io.firestige.iris.vms.gateway.gb28181.server.reactive.SipHeaders;

public interface SipMessage {
    SipHeaders getHeaders();
}

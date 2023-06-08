package io.firestige.iris.vms.gateway.gb28182;

import io.firestige.iris.vms.gateway.gb28182.server.reactive.SipHeaders;

public interface SipMessage {
    SipHeaders getHeaders();
}

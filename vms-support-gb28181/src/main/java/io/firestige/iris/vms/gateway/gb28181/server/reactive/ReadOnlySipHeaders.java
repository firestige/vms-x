package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import org.springframework.util.MultiValueMap;

class ReadOnlySipHeaders extends SipHeaders {
    public ReadOnlySipHeaders(MultiValueMap<String, String> headers) {
        super(headers);
    }
}

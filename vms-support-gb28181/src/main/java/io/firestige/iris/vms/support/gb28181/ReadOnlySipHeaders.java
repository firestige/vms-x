package io.firestige.iris.vms.support.gb28181;

import org.springframework.util.MultiValueMap;

import java.io.Serial;

/**
 * ReadOnlySipHeaders
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/16
 **/
class ReadOnlySipHeaders extends SipHeaders {
    @Serial
    private static final long serialVersionUID = -8277580307517886192L;

    public ReadOnlySipHeaders(MultiValueMap<String, String> headers) {
        super(headers);
    }
}

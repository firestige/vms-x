package io.firestige.iris.vms.support.gb28181.server;

import io.firestige.iris.vms.support.gb28181.SipOutputMessage;
import io.firestige.iris.vms.support.gb28181.SipStatusCode;

import org.springframework.lang.Nullable;

/**
 * ServerSipResponse
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
public interface ServerSipResponse extends SipOutputMessage {
    boolean setStatusCode(@Nullable SipStatusCode statusCode);

    @Nullable
    SipStatusCode getStatusCode();

    default boolean setRawStatusCode(@Nullable Integer statusCode) {
        return setStatusCode(statusCode == null ? null : SipStatusCode.valueOf(statusCode));
    }
}

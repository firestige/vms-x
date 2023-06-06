package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.gb28181.ReactiveSipOutputMessage;
import io.firestige.iris.vms.gateway.gb28181.SipStatusCode;
import org.springframework.lang.Nullable;

import java.util.Objects;

public interface ServerSipResponse extends ReactiveSipOutputMessage {
    boolean setStatusCode(@Nullable SipStatusCode status);
    SipStatusCode getStatusCode();
    default boolean setRawStatusCode(@Nullable Integer value) {
        return setStatusCode(Objects.isNull(value) ? null : SipStatusCode.valueOf(value));
    }
}

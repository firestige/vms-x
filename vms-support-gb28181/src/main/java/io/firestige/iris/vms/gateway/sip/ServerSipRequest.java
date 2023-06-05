package io.firestige.iris.vms.gateway.sip;

import io.firestige.iris.vms.gateway.gb28181.server.reactive.SipHeaders;
import org.springframework.lang.Nullable;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.function.Consumer;

public interface ServerSipRequest extends SipRequest, ReactiveSipInputMessage {
    String getId();
    @Nullable
    default InetSocketAddress getLocalAddress() {
        return null;
    }

    @Nullable
    default InetSocketAddress getRemoteAddress() {
        return null;
    }

    default ServerSipRequest.Builder mutate() {
        return new DefaultServerSipRequestBuilder(this);
    }

    interface Builder {
        Builder method(SipMethod sipMethod);
        Builder uri(URI uri);
        Builder header(String headerName, String... headerValues);
        Builder headers(Consumer<SipHeaders> headersConsumer);
        Builder remoteAddress(InetSocketAddress remoteAddress);
        ServerSipRequest build();
    }
}

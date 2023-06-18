package io.firestige.iris.vms.support.gb28181.server;

import io.firestige.iris.vms.support.gb28181.SipHeaders;
import io.firestige.iris.vms.support.gb28181.SipInputMessage;
import io.firestige.iris.vms.support.gb28181.SipMethod;
import io.firestige.iris.vms.support.gb28181.SipRequest;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.function.Consumer;

/**
 * ServerSipRequest
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
public interface ServerSipRequest extends SipRequest, SipInputMessage {
    // todo 待完善
    String getId();
    default InetSocketAddress getLocalAddress() {
        return null;
    }

    default InetSocketAddress getRemoteAddress() {
        return null;
    }

    default ServerSipRequest.Builder mutate() {
        return new DefaultServerSipRequestBuilder(this);
    }

    interface Builder {
        Builder method(SipMethod method);
        Builder uri(URI uri);
        Builder header(String headerName, String... headerValues);
        Builder headers(Consumer<SipHeaders> headersConsumer);
        Builder remoteAddress(InetSocketAddress remoteAddress);
        ServerSipRequest build();
    }
}

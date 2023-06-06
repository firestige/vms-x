package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.sip.ng.spring.SipMethod;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.net.URI;

public class ServerSipRequestDecorator implements ServerSipRequest {
    public static <T> T getNativeRequest(ServerSipRequest request) {
        return switch (request) {
            case AbstractServerSipRequest abstractServerSipRequest -> abstractServerSipRequest.getNativeRequest();
            case ServerSipRequestDecorator decorator -> getNativeRequest(decorator.getDelegate());
            default -> throw new IllegalArgumentException(
                    "Can not find native request in " + request.getClass().getName());
        };
    }

    private final ServerSipRequest delegate;

    public ServerSipRequestDecorator(ServerSipRequest delegate) {
        Assert.notNull(delegate, "Delegate is required");
        this.delegate = delegate;
    }

    public ServerSipRequest getDelegate() {
        return delegate;
    }

    @Override
    public String getId() {
        return getDelegate().getId();
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return getDelegate().getBody();
    }

    @Override
    public SipHeaders getHeaders() {
        return getDelegate().getHeaders();
    }

    @Override
    public SipMethod getMethod() {
        return getDelegate().getMethod();
    }

    @Override
    public URI getURI() {
        return getDelegate().getURI();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return getDelegate().getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return getDelegate().getRemoteAddress();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

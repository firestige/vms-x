package xyz.firestige.vmsx.support.gb28181.server;

import reactor.core.publisher.Flux;
import xyz.firestige.vmsx.support.gb28181.SipHeaders;
import xyz.firestige.vmsx.support.gb28181.SipMethod;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * ServerSipRequestDecorator
 *
 * @author firestige
 * @createAt 2023/6/18
 **/
public record ServerSipRequestDecorator(ServerSipRequest delegate) implements ServerSipRequest {
    public static <T> T getNativeRequest(ServerSipRequest request) {
        return switch (request) {
            case AbstractServerSipRequest abstractServerSipRequest -> abstractServerSipRequest.getNativeRequest();
            case ServerSipRequestDecorator serverSipRequestDecorator ->
                    getNativeRequest(serverSipRequestDecorator.delegate());
            default ->
                    throw new IllegalArgumentException("Cannot get native request in " + request.getClass().getName());
        };
    }

    public ServerSipRequestDecorator {
        Assert.notNull(delegate, "Delegate must not be null");
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return delegate().getBody();
    }

    @Override
    public SipHeaders getHeaders() {
        return delegate().getHeaders();
    }

    @Override
    public SipMethod getMethod() {
        return delegate().getMethod();
    }

    @Override
    public URI getUri() {
        return delegate().getUri();
    }

    @Override
    public String getId() {
        return delegate().getId();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return delegate().getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return delegate().getRemoteAddress();
    }
}

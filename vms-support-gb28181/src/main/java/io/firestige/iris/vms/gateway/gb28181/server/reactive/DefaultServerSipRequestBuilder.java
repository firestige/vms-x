package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.sip.ng.spring.SipMethod;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.function.Consumer;

class DefaultServerSipRequestBuilder implements ServerSipRequest.Builder {
    private URI uri;
    private final SipHeaders headers;
    private SipMethod method;
    @Nullable
    private InetSocketAddress remoteAddress;
    private final Flux<DataBuffer> body;

    private final ServerSipRequest originalRequest;
    public DefaultServerSipRequestBuilder(ServerSipRequest original) {
        Assert.notNull(original, "ServerSipRequest is required");

        this.uri = original.getURI();
        this.headers = SipHeaders.writableSipHeaders(original.getHeaders());
        this.method = original.getMethod();
        this.remoteAddress = original.getRemoteAddress();
        this.body = original.getBody();
        this.originalRequest = original;
    }

    @Override
    public ServerSipRequest.Builder method(SipMethod method) {
        Assert.notNull(method, "SipMethod must not be null");
        this.method = method;
        return this;
    }

    @Override
    public ServerSipRequest.Builder uri(URI uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public ServerSipRequest.Builder header(String headerName, String... headerValues) {
        this.headers.put(headerName, Arrays.asList(headerValues));
        return this;
    }

    @Override
    public ServerSipRequest.Builder headers(Consumer<SipHeaders> headersConsumer) {
        Assert.notNull(headersConsumer, "'headersConsumer' must not be null");
        headersConsumer.accept(this.headers);
        return this;
    }

    @Override
    public ServerSipRequest.Builder remoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    @Override
    public ServerSipRequest build() {
        return new MutatedServerSipRequest(uri, method, headers, remoteAddress, body, originalRequest);
    }

    private static class MutatedServerSipRequest extends AbstractServerSipRequest {
        private final InetSocketAddress remoteAddress;
        private final Flux<DataBuffer> body;
        private final ServerSipRequest originalRequest;

        public MutatedServerSipRequest(
                URI uri,
                SipMethod method,
                SipHeaders headers,
                InetSocketAddress remoteAddress,
                Flux<DataBuffer> body,
                ServerSipRequest originalRequest) {
            super(method, uri, headers);
            this.remoteAddress = remoteAddress;
            this.body = body;
            this.originalRequest = originalRequest;
        }

        @Override
        public String getId() {
            return originalRequest.getId();
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return originalRequest.getLocalAddress();
        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return this.remoteAddress;
        }

        @Override
        public <T> T getNativeRequest() {
            return ServerSipRequestDecorator.getNativeRequest(this.originalRequest);
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return this.body;
        }
    }
}

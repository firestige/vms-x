package io.firestige.iris.vms.gateway.sip;

import io.firestige.iris.vms.gateway.gb28181.server.reactive.SipHeaders;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.net.InetSocketAddress;
import java.net.URI;

class DefaultServerSipRequestBuilder implements ServerSipRequest.Builder {
    private URI uri;
    private final SipHeaders headers;
    private SipMethod httpMethod;
    @Nullable
    private InetSocketAddress remoteAddress;
    private final Flux<DataBuffer> body;

    private final ServerSipRequest originalRequest;
    public DefaultServerSipRequestBuilder(ServerSipRequest original) {
        Assert.notNull(original, "ServerSipRequest is required");

        this.uri = original.getURI();
        this.headers = HttpHeaders.writableHttpHeaders(original.getHeaders());
        this.httpMethod = original.getMethod();
        this.contextPath = original.getPath().contextPath().value();
        this.remoteAddress = original.getRemoteAddress();
        this.body = original.getBody();
        this.originalRequest = original;
    }
}

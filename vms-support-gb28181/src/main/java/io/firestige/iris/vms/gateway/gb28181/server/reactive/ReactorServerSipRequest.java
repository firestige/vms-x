package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.sip.ng.spring.SipMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.netty.ChannelOperationsId;
import reactor.netty.Connection;
import reactor.netty.http.server.HttpServerRequest;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicLong;

class ReactorServerSipRequest extends AbstractServerSipRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorServerSipRequest.class);
    private static final AtomicLong LOG_PREFIX_INDEX = new AtomicLong();
    private final HttpServerRequest request;
    private final NettyDataBufferFactory bufferFactory;

    public ReactorServerSipRequest(HttpServerRequest request, NettyDataBufferFactory bufferFactory)
            throws URISyntaxException {
        super(SipMethod.parse(request.method().name()), URI.create(request.uri()),
                new NettyHeadersAdapter(request.requestHeaders()));
        this.request = request;
        this.bufferFactory = bufferFactory;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return request.hostAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return request.remoteAddress();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getNativeRequest() {
        return (T) this.request;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return request.receive().retain().map(bufferFactory::wrap);
    }

    @Override
    protected String initId() {
        if (request instanceof Connection connection) {
            return connection.channel().id().asShortText() + "-" + LOG_PREFIX_INDEX.incrementAndGet();
        }
        return null;
    }

    @Override
    protected String initLogPrefix() {
        String id = null;
        if (this.request instanceof ChannelOperationsId operationsId) {
            id = (LOGGER.isDebugEnabled() ? operationsId.asLongText() : operationsId.asShortText());
        }
        if (id != null) {
            return id;
        }
        if (this.request instanceof Connection connection) {
            return connection.channel().id().asShortText() +
                    "-" + LOG_PREFIX_INDEX.incrementAndGet();
        }
        return getId();
    }
}

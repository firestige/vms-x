package xyz.firestige.vmsx.support.gb28181.server;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ChannelOperationsId;
import reactor.netty.Connection;
import reactor.netty.http.server.HttpServerRequest;
import xyz.firestige.vmsx.support.gb28181.SipMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ReactorServerSipRequest
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
class ReactorServerSipRequest extends AbstractServerSipRequest {
    private static final Logger logger = LoggerFactory.getLogger(ReactorServerSipRequest.class);
    private static final AtomicLong LOG_PREFIX_INDEX = new AtomicLong(0);
    private final HttpServerRequest request;
    private final NettyDataBufferFactory bufferFactory;
    public ReactorServerSipRequest(HttpServerRequest httpServerRequest, NettyDataBufferFactory bufferFactory) {
        super(SipMethod.valueOf(httpServerRequest.method().name()), URI.create(httpServerRequest.uri()),
                new NettyHeadersAdapter(httpServerRequest.requestHeaders()));
        Assert.notNull(bufferFactory, "DataBufferFactory must not be null");
        this.request = httpServerRequest;
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
    public Flux<DataBuffer> getBody() {
        return request.receive().retain().map(this.bufferFactory::wrap);
    }

    @Override
    public <T> T getNativeRequest() {
        return (T) request;
    }

    @NonNull
    @Override
    protected Optional<String> initId() {
        if (this.request instanceof Connection connection) {
            return Optional.of(connection.channel().id().asShortText() +
                    "-" + LOG_PREFIX_INDEX.incrementAndGet());
        }
        return Optional.empty();
    }

    @Override
    protected String initLogPrefix() {
        String id = null;
        if (this.request instanceof ChannelOperationsId operationsId) {
            id = (logger.isDebugEnabled() ? operationsId.asLongText() : operationsId.asShortText());
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

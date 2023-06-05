package io.firestige.iris.vms.gateway.gb28181.server.reactive;

import io.firestige.iris.vms.gateway.sip.ReactiveSipInputMessage;
import io.firestige.iris.vms.gateway.sip.SipMethod;
import io.firestige.iris.vms.gateway.sip.SipServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import reactor.netty.http.server.HttpServerRequest;

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
}

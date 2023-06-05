package io.firestige.iris.vms.gateway.sip;

import io.netty.buffer.ByteBuf;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.ConnectionObserver;
import reactor.netty.NettyInbound;
import reactor.netty.NettyOutbound;
import reactor.netty.channel.AbortedException;
import reactor.netty.channel.ChannelOperations;
import reactor.netty.http.logging.HttpMessageLogFactory;

public abstract class SipOperations<INBOUND extends NettyInbound, OUTBOUND extends NettyOutbound>
        extends ChannelOperations<INBOUND, OUTBOUND> implements SipInfos {
    volatile int statusAndHeadersSent;

    static final int READY = 0;
    static final int HEADERS_SENT = 1;
    static final int BODY_SENT = 2;

    final HttpMessageLogFactory httpMessageLogFactory;

    public SipOperations(SipOperations<INBOUND, OUTBOUND> replaced) {
        super(replaced);
        this.httpMessageLogFactory = replaced.httpMessageLogFactory;
        this.statusAndHeadersSent = replaced.statusAndHeadersSent;
    }

    public SipOperations(Connection connection,
                         ConnectionObserver listener,
                         HttpMessageLogFactory httpMessageLogFactory) {
        super(connection, listener);
        this.httpMessageLogFactory = httpMessageLogFactory;
    }

    public final boolean hasSentHeaders() {
        return statusAndHeadersSent != READY;
    }

    @Override
    public NettyOutbound send(Publisher<? extends ByteBuf> source) {
        if (!channel().isActive()) {
            return then(Mono.error(AbortedException.beforeSend()));
        }
        if (source instanceof Mono) {
            return null;
        }
        return super.send(source);
    }
}

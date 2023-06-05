package io.firestige.iris.vms.gateway.sip;

import io.firestige.iris.vms.gateway.gb28181.server.reactive.SipHeaders;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.NettyOutbound;

import java.util.function.Consumer;

public interface SipServerResponse extends NettyOutbound, SipServerInfos {
    SipServerResponse addHeader(CharSequence name, CharSequence value);
    SipServerResponse chunkedTransfer(boolean chunked);
    @Override
    SipServerResponse withConnection(Consumer<? super Connection> withConnection);
    boolean hasSentHeaders();
    SipServerResponse headers(SipHeaders headers);
    SipHeaders responseHeaders();
    Mono<Void> send();
    NettyOutbound sendHeaders();
    Mono<Void> sendNotFound();
    SipResponseStatus status();
    default SipResponseStatus status(int status) {
        return status(SipResponseStatus.valueOf(status));
    }
    SipServerResponse trailerHeaders(Consumer<? super SipHeaders> trailerHeaders);
}

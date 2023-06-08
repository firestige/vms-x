package io.firestige.iris.vms.gateway.gb28182.server.reactive;

import org.reactivestreams.Publisher;
import reactor.core.CoreSubscriber;
import reactor.core.Scannable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

//todo 待补齐
public class ChannelSendOperator<T> extends Mono<Void> implements Scannable {
    private final Function<Publisher<T>, Publisher<Void>> writeFunction;

    private final Flux<T> source;
    public ChannelSendOperator(Publisher<? extends T> source, Function<Publisher<T>, Publisher<Void>> writeFunction) {
        this.source = Flux.from(source);
        this.writeFunction = writeFunction;
    }

    @Override
    public Object scanUnsafe(Attr attr) {
        return null;
    }

    @Override
    public void subscribe(CoreSubscriber<? super Void> coreSubscriber) {

    }
}

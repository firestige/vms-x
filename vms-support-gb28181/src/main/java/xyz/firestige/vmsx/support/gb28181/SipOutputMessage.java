package xyz.firestige.vmsx.support.gb28181;

import reactor.core.publisher.Mono;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;

import java.util.function.Supplier;

/**
 * SipOutputMessage
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
public interface SipOutputMessage extends SipMessage {
    /**
     * 返回一个DataBufferFactory，用于创建Body
     *
     * @return 一个Buffer Factory
     */
    DataBufferFactory  bufferFactory();

    /**
     * 出则一个提交前的操作
     *
     * @param action 操作
     */
    void beforeCommit(Supplier<? extends Mono<Void>> action);

    /**
     * 是否已提交
     *
     * @return true-已提交，false-未提交
     */
    boolean isCommitted();

    /**
     * 写入Body
     *
     * @param body Body
     * @return 一个Mono，表示写入完成或者失败
     */
    Mono<Void> writeWith(Publisher<? extends DataBuffer> body);

    /**
     * 写入Body并冲刷缓冲区
     *
     * @param body body
     * @return 一个Mono，表示写入完成或者失败
     */
    Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body);

    /**
     * 当处理完毕时调用，可用于触发清理动作
     *
     * @return 一个Mono，表示处理完毕或失败
     */
    Mono<Void> setComplete();
}

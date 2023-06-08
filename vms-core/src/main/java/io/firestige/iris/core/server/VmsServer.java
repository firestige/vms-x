package io.firestige.iris.core.server;

import java.util.function.Consumer;

/**
 * 服务器控制接口
 */
public interface VmsServer {
    /**
     * 启动
     *
     * @throws ServerException 当服务器无法启动应抛出此异常
     */
    void start() throws ServerException;

    /**
     * 停止
     *
     * @throws ServerException 当服务器无法停止抛出此异常
     */
    void stop() throws ServerException;

    /**
     * 返回监听的端口
     *
     * @return 没有监听端口返回-1
     */
    int getPort();

    /**
     * 优雅关机，调用后应停止处理新的请求，视策略处理当前请求，处理完后调用{@link VmsServer#stop}关闭
     *
     * @param callback 停机时要执行的回调
     */
    default void shutdown(Consumer<GracefullyShutdownStrategy> callback) {
        callback.accept(GracefullyShutdownStrategy.IMMEDIATE);
    }
}

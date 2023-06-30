package xyz.firestige.vmsx.core.server;

/**
 * 优雅关闭策略
 */
public enum GracefullyShutdownStrategy {
    /**
     * 立即关闭
     */
    IMMEDIATE,
    /**
     * 所有请求完成后关闭
     */
    AFTER_ALL_REQUEST_FINISHED,
    /**
     * 等待一段时间后关闭
     */
    IN_PERIOD;
}

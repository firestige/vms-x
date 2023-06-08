package io.firestige.iris.core.server;

/**
 * 关机策略
 */
public enum ShutdownStrategy {
    /**
     * 优雅关机
     */
    GRACEFUL,
    /**
     * 立即关闭
     */
    IMMEDIATE;
}

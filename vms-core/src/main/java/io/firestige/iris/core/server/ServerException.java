package io.firestige.iris.core.server;

/**
 * 服务器异常
 */
public class ServerException extends RuntimeException {
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}

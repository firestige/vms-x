package io.firestige.iris.core.server;

/**
 * VmsServerFactoryCustomizer
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/16
 **/
@FunctionalInterface
public interface VmsServerFactoryCustomizer<T extends VmsServerFactory> {
    void customize(T factory);
}

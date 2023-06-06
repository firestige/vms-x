package io.firestige.iris.vms.gateway.gb28181;

import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;

public interface ZeroCopySipOutputMessage extends ReactiveSipOutputMessage {
    Mono<Void> writeWith(Path file, long position, long count);
    default Mono<Void> writeWith(File file, long position, long count) {
        return writeWith(file.toPath(), position, count);
    }
}

package io.firestige.iris.vms.gateway.device;

import java.util.List;

import io.firestige.iris.vms.gateway.gb28181.context.type.Status;
import reactor.core.publisher.Mono;

public interface DeviceService {
    Mono<Void> keepalive(String deviceId, Status Status, List<String> errorList);
}

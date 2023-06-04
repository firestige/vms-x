package io.firestige.iris.vms.gateway.device;

import java.util.List;

import io.firestige.iris.vms.gateway.gb28181.context.type.Status;
import reactor.core.publisher.Mono;

public class CenterControlServer implements DeviceService {

    @Override
    public Mono<Void> keepalive(String deviceId, Status Status, List<String> errorList) {
        return Mono.empty();
    }

}

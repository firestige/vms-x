package io.firestige.iris.vms.gateway.device;

import java.util.List;

import io.firestige.iris.vms.gateway.gb28182.context.type.Status;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CenterControlServer implements DeviceService {

    @Override
    public Mono<Void> keepalive(String deviceId, Status Status, List<String> errorList) {
        return Mono.empty();
    }

}

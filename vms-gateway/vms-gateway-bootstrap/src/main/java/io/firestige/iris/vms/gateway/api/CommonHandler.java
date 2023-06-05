package io.firestige.iris.vms.gateway.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.firestige.iris.vms.gateway.device.DeviceService;
import io.firestige.iris.vms.gateway.gb28181.context.annotations.CmdType;
import io.firestige.iris.vms.gateway.gb28181.context.type.Status;
import io.firestige.iris.vms.gateway.gb28181.server.context.Gb28181Response;
import io.firestige.iris.vms.gateway.gb28181.server.handler.annotations.MessageMapping;
import io.firestige.iris.vms.gateway.gb28181.server.handler.annotations.Params;
import io.firestige.iris.vms.gateway.gb28181.server.handler.annotations.RequestHandler;
import reactor.core.publisher.Mono;

@RequestHandler
public class CommonHandler {

    @Autowired
    private DeviceService service;

    @MessageMapping(type = "Notify")
    @CmdType("Keepalive")
    public Mono<Gb28181Response> keepalive(@Params("DeviceID") String deviceId, @Params("Status") Status Status, @Params("Info") List<String> errorList) {
        return service.keepalive(deviceId, Status, errorList)
            .thenReturn(Gb28181Response.ok());
    }
}

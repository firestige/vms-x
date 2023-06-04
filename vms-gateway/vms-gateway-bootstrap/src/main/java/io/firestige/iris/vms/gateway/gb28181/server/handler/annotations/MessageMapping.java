package io.firestige.iris.vms.gateway.gb28181.server.handler.annotations;

@Mapping(method = "Message")
public @interface MessageMapping {
    String type();
}

package io.firestige.iris.vms.gateway.gb28182.server.handler.annotations;

@Mapping(method = "Message")
public @interface MessageMapping {
    String type();
}

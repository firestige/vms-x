package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server.sip", ignoreInvalidFields = true)
public class SipServerProperties {
}

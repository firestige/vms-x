package io.firestige.iris.vms.gateway.gb28181.autoconfigure;

import io.firestige.iris.vms.gateway.gb28181.server.SipServerFactory;
import io.firestige.iris.vms.gateway.gb28181.server.SipServerFactoryCustomizer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.core.Ordered;

public class ReactiveSipServerFactoryCustomizer implements SipServerFactoryCustomizer<SipServerFactory>, Ordered {
    private final SipServerProperties serverProperties;

    public ReactiveSipServerFactoryCustomizer(SipServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Override
    public void customize(SipServerFactory factory) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(this.serverProperties::getPort).to(factory::setPort);
        map.from(this.serverProperties::getAddress).to(factory::setAddress);
        map.from(this.serverProperties::getShutdown).to(factory::setShutdownStrategy);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

package io.firestige.iris.vms.support.gb28181.server.embedded;

import io.firestige.iris.core.server.VmsServerFactoryCustomizer;
import io.firestige.iris.vms.support.gb28181.autoconfigure.GB28181Properties;

import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.core.Ordered;

/**
 * NettyGB28181ServerFactoryCustomizer
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/16
 **/
public class NettySipServerFactoryCustomizer implements VmsServerFactoryCustomizer<NettySipServerFactory>, Ordered {
    private final GB28181Properties properties;

    public NettySipServerFactoryCustomizer(GB28181Properties properties) {
        this.properties = properties;
    }

    @Override
    public void customize(NettySipServerFactory factory) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        map.from(properties::getPort).to(factory::setPort);
        map.from(properties::getAddress).to(factory::setAddress);
        map.from(properties.getShutdownStrategy()).to(factory::setShutdown);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

package xyz.firestige.vmsx.support.gb28181.context;

import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * GB28181ApplicationContextFactory
 * 注册到spring.factories，用于提供自动创建GB28181上下文
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
class GB28181ApplicationContextFactory implements ApplicationContextFactory {

    @Override
    public Class<? extends ConfigurableEnvironment> getEnvironmentType(WebApplicationType webApplicationType) {
        return GB28181Environment.class;
    }

    @Override
    public ConfigurableEnvironment createEnvironment(WebApplicationType webApplicationType) {
        return new GB28181Environment();
    }

    @Override
    public ConfigurableApplicationContext create(WebApplicationType webApplicationType) {
        return new GB28181ApplicationContext();
    }
}

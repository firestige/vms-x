package io.firestige.iris.vms.support.gb28181.autoconfigure;

import io.firestige.iris.vms.support.gb28181.SipInputMessage;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * GB28181ServerFactoryAutoConfiguration
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfiguration
@ConditionalOnClass(SipInputMessage.class)
@EnableConfigurationProperties(GB28181Properties.class)
@Import({SipServerFactoryConfiguration.class})
public class GB28181ServerFactoryAutoConfiguration {
}

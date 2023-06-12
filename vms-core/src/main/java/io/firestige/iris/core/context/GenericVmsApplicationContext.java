package io.firestige.iris.core.context;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;


/**
 * GenericVmsApplicationContext
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 */
public class GenericVmsApplicationContext  extends GenericApplicationContext implements ConfigurableVmsApplicationContext {
    public GenericVmsApplicationContext() {
    }

    public GenericVmsApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    protected ConfigurableEnvironment createEnvironment() {
        return new StandardVmsEnvironment();
    }
}

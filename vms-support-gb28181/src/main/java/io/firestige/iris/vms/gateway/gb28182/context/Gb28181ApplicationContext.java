package io.firestige.iris.vms.gateway.gb28182.context;

import io.firestige.iris.vms.gateway.gb28182.server.Gb28181Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ObjectUtils;

public interface Gb28181ApplicationContext extends ConfigurableApplicationContext {
    Gb28181Server getGb28181Server();

    String getServerNamespace();

    void setServerNamespace(String namespace);

    static boolean hasSeverNamespace(ApplicationContext context, String namespace) {
        return (context instanceof Gb28181ApplicationContext gb28181ApplicationContext)
                && ObjectUtils.nullSafeEquals(gb28181ApplicationContext.getServerNamespace(), namespace);
    }

    static String getServerNamespace(ApplicationContext context) {
        return (context instanceof Gb28181ApplicationContext configurableContext) ?
                configurableContext.getServerNamespace() : null;
    }
}

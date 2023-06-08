package io.firestige.iris.vms.gateway.gb28181.context;

import io.firestige.iris.vms.gateway.gb28181.server.SipServer;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

public interface SipServerApplicationContext extends ApplicationContext {
    SipServer getSipServer();
    String getServerNamespace();
    static boolean hasServerNamespace(ApplicationContext context, String serverNamespace) {
        return (context instanceof SipServerApplicationContext gb28181ApplicationContext)
                && ObjectUtils.nullSafeEquals(gb28181ApplicationContext.getServerNamespace(), serverNamespace);
    }
    static String getServerNamespace(ApplicationContext context) {
        return (context instanceof SipServerApplicationContext configurableContext)
                ? configurableContext.getServerNamespace() : null;

    }
}

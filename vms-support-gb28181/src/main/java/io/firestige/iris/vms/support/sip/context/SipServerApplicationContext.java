package io.firestige.iris.vms.support.sip.context;

import io.firestige.iris.vms.support.sip.server.SipServer;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;

/**
 * SipServerApplicationContext
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/11
 **/
public interface SipServerApplicationContext extends ApplicationContext {
    SipServer getSipServer();
    String getServerNamespace();
    static boolean hasServerNamespace(ApplicationContext ctx, String serverNamespace) {
        return ctx instanceof SipServerApplicationContext sipServerApplicationContext
                && ObjectUtils.nullSafeEquals(sipServerApplicationContext.getServerNamespace(), serverNamespace);
    }
    static String getServerNamespace(ApplicationContext ctx) {
        return ctx instanceof SipServerApplicationContext configurableContext
                ? configurableContext.getServerNamespace() : null;
    }
}

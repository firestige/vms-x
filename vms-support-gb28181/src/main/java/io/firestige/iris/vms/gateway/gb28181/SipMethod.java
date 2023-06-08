package io.firestige.iris.vms.gateway.gb28181;

import java.util.Locale;

public enum SipMethod {
    REGISTER;
    public static SipMethod resolve(String method) {
        return SipMethod.valueOf(method.toUpperCase(Locale.ENGLISH));
    }
}

package io.firestige.iris.vms.gateway.sip;

import org.springframework.util.Assert;

import java.util.Locale;

public enum SipMethod {
    REGISTER, MESSAGE, INVITE, ACK, INFO, BYE, CANCEL, SUBSCRIBE, NOTIFY, UNKNOWN;

    public static SipMethod parse(String method) {
        Assert.notNull(method, "Method must not be null");
        String upperMethod = method.toUpperCase(Locale.ENGLISH);
        return switch (upperMethod) {
            case "REGISTER" -> REGISTER;
            case "MESSAGE" -> MESSAGE;
            case "INVITE" -> INVITE;
            case "ACK" -> ACK;
            case "INFO" -> INFO;
            case "BYE" -> BYE;
            case "CANCEL" -> CANCEL;
            case "SUBSCRIBE" -> SUBSCRIBE;
            case "NOTIFY" -> NOTIFY;
            default -> UNKNOWN;
        };
    }

    public boolean matches(String method) {
        return name().equalsIgnoreCase(method);
    }


}

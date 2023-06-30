package xyz.firestige.vmsx.support.gb28181;

/**
 * SipMethod
 *
 * @author firestige
 * @createAt 2023/6/17
 **/
public enum SipMethod {
    REGISTER("REGISTER"),
    INVITE("INVITE"),
    CANCEL("CANCEL"),
    ACK("ACK"),
    INFO("INFO"),
    BYE("BYE"),
    MESSAGE("MESSAGE"),
    SUBSCRIBE("SUBSCRIBE"),
    NOTIFY("NOTIFY");

    private final String name;

    SipMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean matches(String method) {
        return name.equalsIgnoreCase(method);
    }
}

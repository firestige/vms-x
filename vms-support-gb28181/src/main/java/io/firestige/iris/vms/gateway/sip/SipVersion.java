package io.firestige.iris.vms.gateway.sip;

import io.netty.handler.codec.http.HttpVersion;

public class SipVersion extends HttpVersion {

    public static final String SIP_2_0_STRING = "SIP/2.0";
    public static final SipVersion SIP_2_0 = new SipVersion("SIP", 2, 0);

    public static SipVersion valueOf(String text) {
        if (SIP_2_0_STRING.equalsIgnoreCase(text)) {
            return SIP_2_0;
        }
        return new SipVersion(text);
    }

    public SipVersion(String text) {
        super(text, false);
    }

    public SipVersion(String protocolName, int majorVersion, int minorVersion) {
        super(protocolName, majorVersion, minorVersion, false);
    }
}

package io.firestige.iris.vms.gateway.sip;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Optional;

public class SipResponseStatus extends HttpResponseStatus {
    public static final SipResponseStatus TRYING = newStatus(100, "Trying");
    public static final SipResponseStatus RINGING  = newStatus(180, "Ringing");
    public static final SipResponseStatus CALL_IS_BEING_FORWARDED = newStatus(181, "Call Is Being Forwarded");
    public static final SipResponseStatus QUEUED   = newStatus(182, "Queued ");
    public static final SipResponseStatus SESSION_PROGRESS  = newStatus(183, "Session Progress");
    public static final SipResponseStatus OK  = newStatus(200, "OK");
    public static final SipResponseStatus MULTIPLE_CHOICES  = newStatus(300, "Multiple Choices");
    public static final SipResponseStatus MOVED_PERMANENTLY  = newStatus(301, "Moved Permanently");
    public static final SipResponseStatus MOVED_TEMPORARILY  = newStatus(302, "Moved Temporarily");
    public static final SipResponseStatus USE_PROXY  = newStatus(305, "Use Proxy");
    public static final SipResponseStatus ALTERNATIVE_SERVICE  = newStatus(380, "Alternative Service");
    public static final SipResponseStatus BAD_REQUEST  = newStatus(400, "Bad Request");
    public static final SipResponseStatus UNAUTHORIZED   = newStatus(401, "Unauthorized");
    public static final SipResponseStatus PAYMENT_REQUIRED  = newStatus(402, "Payment Required");
    public static final SipResponseStatus FORBIDDEN = newStatus(403, "Forbidden");
    public static final SipResponseStatus NOT_FOUND = newStatus(404, "Not Found");
    public static final SipResponseStatus METHOD_NOT_ALLOWED = newStatus(405, "Method Not Allowed");
    public static final SipResponseStatus NOT_ACCEPTABLE = newStatus(406, "Not Acceptable");
    public static final SipResponseStatus PROXY_AUTHENTICATION_REQUIRED = newStatus(407, "Proxy Authentication Required");
    public static final SipResponseStatus REQUEST_TIMEOUT = newStatus(408, "Request Timeout");
    public static final SipResponseStatus GONE = newStatus(410, "GONE");
    public static final SipResponseStatus REQUEST_ENTITY_TOO_LARGE = newStatus(410, "Request Entity Too Large");
    public static final SipResponseStatus REQUEST_URI_TOO_LONG = newStatus(413, "Request-URI Too Long");
    public static final SipResponseStatus UNSUPPORTED_MEDIA_TYPE = newStatus(414, "Unsupported Media Type");
    public static final SipResponseStatus UNSUPPORTED_URI_SCHEME = newStatus(415, "Unsupported URI Scheme");


    private static SipResponseStatus newStatus(int code, String reason) {
        return new SipResponseStatus(code, reason);
    }

    public static SipResponseStatus valueOf(int code) {
        return Optional.ofNullable(valueOf0(code)).orElseGet(() -> new SipResponseStatus(code));
    }

    private static SipResponseStatus valueOf0(int code) {
        switch (code) {
            case 100:
            case 101:
        }
    }

    public SipResponseStatus(int code) {
        super(code, SipStatusClass.valueOf(code).defaultReasonPhrase() + " (" + code + ')');
    }

    /**
     * Creates a new instance with the specified {@code code} and its {@code reasonPhrase}.
     *
     * @param code
     * @param reasonPhrase
     */
    public SipResponseStatus(int code, String reasonPhrase) {
        super(code, reasonPhrase);
    }
}

package io.firestige.iris.vms.gateway.sip;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;

import java.util.Optional;

import static java.lang.Integer.parseInt;

public class SipResponseStatus extends HttpResponseStatus {
    /**
     * 100 Trying
     * <p>
     *    This response indicates that the request has been received by the
     *    next-hop server and that some unspecified action is being taken on
     *    behalf of this call (for example, a database is being consulted).
     *    This response, like all other provisional responses, stops
     *    retransmissions of an INVITE by a UAC.  The 100 (Trying) response is
     *    different from other provisional responses, in that it is never
     *    forwarded upstream by a stateful proxy.
     */
    public static final SipResponseStatus TRYING = newStatus(100, "Trying");
    /**
     * 180 Ringing
     * <p>
     *    The UA receiving the INVITE is trying to alert the user.  This
     *    response MAY be used to initiate local ringback.
     */
    public static final SipResponseStatus RINGING  = newStatus(180, "Ringing");
    /**
     * 181 Call Is Being Forwarded
     * <p>
     *    A server MAY use this status code to indicate that the call is being
     *    forwarded to a different set of destinations.
     */
    public static final SipResponseStatus CALL_IS_BEING_FORWARDED = newStatus(181, "Call Is Being Forwarded");
    /**
     * 182 Queued
     * <p>
     *    The called party is temporarily unavailable, but the server has
     *    decided to queue the call rather than reject it.  When the callee
     *    becomes available, it will return the appropriate final status
     *    response.  The reason phrase MAY give further details about the
     *    status of the call, for example, "5 calls queued; expected waiting
     *    time is 15 minutes".  The server MAY issue several 182 (Queued)
     *    responses to update the caller about the status of the queued call.
     */
    public static final SipResponseStatus QUEUED = newStatus(182, "Queued ");
    /**
     * 183 Session Progress
     * <p>
     *    The 183 (Session Progress) response is used to convey information
     *    about the progress of the call that is not otherwise classified.  The
     *    Reason-Phrase, header fields, or message body MAY be used to convey
     *    more details about the call progress.
     */
    public static final SipResponseStatus SESSION_PROGRESS = newStatus(183, "Session Progress");
    /**
     * 200 OK
     * <p>
     *    The request has succeeded.  The information returned with the
     *    response depends on the method used in the request.
     */
    public static final SipResponseStatus OK = newStatus(200, "OK");
    /**
     * Redirection 3xx
     * <p>
     *    3xx responses give information about the user's new location, or
     *    about alternative services that might be able to satisfy the call.
     */
    public static final SipResponseStatus MULTIPLE_CHOICES = newStatus(300, "Multiple Choices");
    /**
     * 300 Multiple Choices
     * <p>
     *    The address in the request resolved to several choices, each with its
     *    own specific location, and the user (or UA) can select a preferred
     *    communication end point and redirect its request to that location.
     * <p>
     *    The response MAY include a message body containing a list of resource
     *    characteristics and location(s) from which the user or UA can choose
     *    the one most appropriate, if allowed by the Accept request header
     *    field.  However, no MIME types have been defined for this message
     *    body.
     * <p>
     *    The choices SHOULD also be listed as Contact fields (Section 20.10).
     *    Unlike HTTP, the SIP response MAY contain several Contact fields or a
     *    list of addresses in a Contact field.  UAs MAY use the Contact header
     *    field value for automatic redirection or MAY ask the user to confirm
     *    a choice.  However, this specification does not define any standard
     *    for such automatic selection.
     * <p>
     *       This status response is appropriate if the callee can be reached
     *       at several different locations and the server cannot or prefers
     *       not to proxy the request.
     */
    public static final SipResponseStatus MOVED_PERMANENTLY = newStatus(301, "Moved Permanently");
    /**
     * 301 Moved Permanently
     *
     *    The user can no longer be found at the address in the Request-URI,
     *    and the requesting client SHOULD retry at the new address given by
     *    the Contact header field (Section 20.10).  The requestor SHOULD
     *    update any local directories, address books, and user location caches
     *    with this new value and redirect future requests to the address(es)
     *    listed.
     */
    public static final SipResponseStatus MOVED_TEMPORARILY = newStatus(302, "Moved Temporarily");
    public static final SipResponseStatus USE_PROXY = newStatus(305, "Use Proxy");
    public static final SipResponseStatus ALTERNATIVE_SERVICE = newStatus(380, "Alternative Service");
    public static final SipResponseStatus BAD_REQUEST = newStatus(400, "Bad Request");
    public static final SipResponseStatus UNAUTHORIZED = newStatus(401, "Unauthorized");
    public static final SipResponseStatus PAYMENT_REQUIRED = newStatus(402, "Payment Required");
    public static final SipResponseStatus FORBIDDEN = newStatus(403, "Forbidden");
    public static final SipResponseStatus NOT_FOUND = newStatus(404, "Not Found");
    public static final SipResponseStatus METHOD_NOT_ALLOWED = newStatus(405, "Method Not Allowed");
    public static final SipResponseStatus NOT_ACCEPTABLE = newStatus(406, "Not Acceptable");
    public static final SipResponseStatus PROXY_AUTHENTICATION_REQUIRED = newStatus(407, "Proxy Authentication Required");
    public static final SipResponseStatus REQUEST_TIMEOUT = newStatus(408, "Request Timeout");
    public static final SipResponseStatus GONE = newStatus(410, "GONE");
    public static final SipResponseStatus REQUEST_ENTITY_TOO_LARGE = newStatus(413, "Request Entity Too Large");
    public static final SipResponseStatus REQUEST_URI_TOO_LONG = newStatus(414, "Request-URI Too Long");
    public static final SipResponseStatus UNSUPPORTED_MEDIA_TYPE = newStatus(415, "Unsupported Media Type");
    public static final SipResponseStatus UNSUPPORTED_URI_SCHEME = newStatus(416, "Unsupported URI Scheme");
    public static final SipResponseStatus BAD_EXTENSION = newStatus(420, "Bad Extension");
    public static final SipResponseStatus EXTENSION_REQUIRED = newStatus(421, "Extension Required");
    public static final SipResponseStatus INTERVAL_TOO_BRIEF = newStatus(423, "Interval Too Brief");
    public static final SipResponseStatus TEMPORARILY_UNAVAILABLE = newStatus(480, "Temporarily Unavailable");
    public static final SipResponseStatus CALL_TRANSACTION_DOES_NOT_EXIST = newStatus(481, "Call/Transaction Does " +
                                                                                               "Not Exist");
    public static final SipResponseStatus LOOP_DETECTED = newStatus(482, "Loop Detected");
    public static final SipResponseStatus TOO_MANY_HOPS = newStatus(483, "Too Many Hops");
    public static final SipResponseStatus ADDRESS_INCOMPLETE = newStatus(484, "Address Incomplete");
    public static final SipResponseStatus AMBIGUOUS = newStatus(485, "Ambiguous");
    public static final SipResponseStatus BUSY_HERE = newStatus(486, "Busy Here");
    public static final SipResponseStatus REQUEST_TERMINATED = newStatus(487, "Request Terminated");
    public static final SipResponseStatus NOT_ACCEPTABLE_HERE = newStatus(488, "Not Acceptable Here");
    public static final SipResponseStatus REQUEST_PENDING = newStatus(491, "Request Pending");
    public static final SipResponseStatus UNDECIPHERABLE = newStatus(493, "Undecipherable");
    public static final SipResponseStatus SERVER_INTERNAL_ERROR = newStatus(500, "Server Internal Error");
    public static final SipResponseStatus NOT_IMPLEMENTED = newStatus(501, "Not Implemented");
    public static final SipResponseStatus BAD_GATEWAY = newStatus(502, "Bad Gateway");
    public static final SipResponseStatus SERVICE_UNAVAILABLE = newStatus(503, "Service Unavailable");
    public static final SipResponseStatus SERVER_TIMEOUT = newStatus(504, "Server Time-out");
    public static final SipResponseStatus VERSION_NOT_SUPPORTED = newStatus(505, "Version Not Supported");
    public static final SipResponseStatus MESSAGE_TOO_LARGE = newStatus(513, "Message Too Large");
    public static final SipResponseStatus BUSY_EVERYWHERE = newStatus(600, "Busy Everywhere");
    public static final SipResponseStatus DECLINE = newStatus(603, "Decline");
    public static final SipResponseStatus DOES_NOT_EXIST_ANYWHERE = newStatus(604, "Does Not Exist Anywhere");
    public static final SipResponseStatus NOT_ACCEPTABLE_ = newStatus(606, "Not Acceptable");

    private static SipResponseStatus newStatus(int code, String reason) {
        return new SipResponseStatus(code, reason);
    }

    public static SipResponseStatus valueOf(int code) {
        return Optional.ofNullable(valueOf0(code)).orElseGet(() -> new SipResponseStatus(code));
    }

    public static SipResponseStatus valueOf(int code, String reasonPhrase) {
        return Optional.ofNullable(valueOf0(code))
                .filter(status -> status.reasonPhrase().contentEquals(reasonPhrase))
                .orElseGet(() -> new SipResponseStatus(code, reasonPhrase));
    }

    private static SipResponseStatus valueOf0(int code) {
        return switch (code) {
            case 100 -> TRYING;
            case 180 -> RINGING;
            case 181 -> CALL_IS_BEING_FORWARDED;
            case 182 -> QUEUED;
            case 183 -> SESSION_PROGRESS;
            case 200 -> OK;
            case 300 -> MULTIPLE_CHOICES;
            case 301 -> MOVED_PERMANENTLY;
            case 302 -> MOVED_TEMPORARILY;
            case 305 -> USE_PROXY;
            case 380 -> ALTERNATIVE_SERVICE;
            case 400 -> BAD_REQUEST;
            case 401 -> UNAUTHORIZED;
            case 402 -> PAYMENT_REQUIRED;
            case 403 -> FORBIDDEN;
            case 404 -> NOT_FOUND;
            case 405 -> METHOD_NOT_ALLOWED;
            case 406 -> NOT_ACCEPTABLE;
            case 407 -> PROXY_AUTHENTICATION_REQUIRED;
            case 408 -> REQUEST_TIMEOUT;
            case 410 -> GONE;
            case 413 -> REQUEST_ENTITY_TOO_LARGE;
            case 414 -> REQUEST_URI_TOO_LONG;
            case 415 -> UNSUPPORTED_MEDIA_TYPE;
            case 416 -> UNSUPPORTED_URI_SCHEME;
            case 420 -> BAD_EXTENSION;
            case 421 -> EXTENSION_REQUIRED;
            case 423 -> INTERVAL_TOO_BRIEF;
            case 480 -> TEMPORARILY_UNAVAILABLE;
            case 481 -> CALL_TRANSACTION_DOES_NOT_EXIST;
            case 482 -> LOOP_DETECTED;
            case 483 -> TOO_MANY_HOPS;
            case 484 -> ADDRESS_INCOMPLETE;
            case 485 -> AMBIGUOUS;
            case 486 -> BUSY_HERE;
            case 487 -> REQUEST_TERMINATED;
            case 488 -> NOT_ACCEPTABLE_HERE;
            case 491 -> REQUEST_PENDING;
            case 493 -> UNDECIPHERABLE;
            case 500 -> SERVER_INTERNAL_ERROR;
            case 501 -> NOT_IMPLEMENTED;
            case 502 -> BAD_GATEWAY;
            case 503 -> SERVICE_UNAVAILABLE;
            case 504 -> SERVER_TIMEOUT;
            case 505 -> VERSION_NOT_SUPPORTED;
            case 513 -> MESSAGE_TOO_LARGE;
            case 600 -> BUSY_EVERYWHERE;
            case 603 -> DECLINE;
            case 604 -> DOES_NOT_EXIST_ANYWHERE;
            case 606 -> NOT_ACCEPTABLE_;
            default -> new SipResponseStatus(code);
        };
    }

    public static SipResponseStatus parseLine(CharSequence line) {
        return (line instanceof AsciiString) ? parseLine((AsciiString) line) : parseLine(line.toString());
    }

    public static SipResponseStatus parseLine(AsciiString line) {
        try {
            int space = line.forEachByte(ByteProcessor.FIND_ASCII_SPACE);
            return space == -1 ? valueOf(line.parseInt()) : valueOf(line.parseInt(0, space), line.toString(space  + 1));
        } catch (Exception e) {
            throw new IllegalArgumentException("malformed status line: " + line, e);
        }
    }

    public static SipResponseStatus parseLine(String line) {
        try {
            int space = line.indexOf(' ');
            return space == -1 ? valueOf(parseInt(line)) :
                    valueOf(parseInt(line.substring(0, space)), line.substring(space + 1));
        } catch (Exception e) {
            throw new IllegalArgumentException("malformed status line: " + line, e);
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

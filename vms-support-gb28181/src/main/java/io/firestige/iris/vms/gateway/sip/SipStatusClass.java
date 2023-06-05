package io.firestige.iris.vms.gateway.sip;

import io.netty.util.AsciiString;

public enum SipStatusClass {
    PROVISIONAL (100, 200, "Provisional"),
    /**
     * The success class (2xx)
     */
    SUCCESSFUL (200, 300, "Successful"),
    /**
     * The redirection class (3xx)
     */
    REDIRECTION (300, 400, "Redirection"),
    /**
     * The client error class (4xx)
     */
    REQUEST_FAILURE (400, 500, "Request Failure"),
    /**
     * The server error class (5xx)
     */
    SERVER_FAILURE(500, 600, "Server Failure"),
    GLOBAL_FAILURES(600, 607, "Global Failures"),
    /**
     * The unknown class
     */
    UNKNOWN(0, 0, "Unknown Status") {
        @Override
        public boolean contains(int code) {
            return code < 100 || code >= 607;
        }
    };

    /**
     * Returns the class of the specified HTTP status code.
     */
    public static SipStatusClass valueOf(int code) {
        if (PROVISIONAL.contains(code)) {
            return PROVISIONAL;
        }
        if (SUCCESSFUL.contains(code)) {
            return SUCCESSFUL;
        }
        if (REDIRECTION.contains(code)) {
            return REDIRECTION;
        }
        if (REQUEST_FAILURE.contains(code)) {
            return REQUEST_FAILURE;
        }
        if (SERVER_FAILURE.contains(code)) {
            return SERVER_FAILURE;
        }
        if (GLOBAL_FAILURES.contains(code)) {
            return GLOBAL_FAILURES;
        }
        return UNKNOWN;
    }

    /**
     * Returns the class of the specified HTTP status code.
     * @param code Just the numeric portion of the http status code.
     */
    public static SipStatusClass valueOf(CharSequence code) {
        if (code != null && code.length() == 3) {
            char c0 = code.charAt(0);
            return isDigit(c0) && isDigit(code.charAt(1)) && isDigit(code.charAt(2)) ? valueOf(digit(c0) * 100)
                    : UNKNOWN;
        }
        return UNKNOWN;
    }

    private static int digit(char c) {
        return c - '0';
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private final int min;
    private final int max;
    private final AsciiString defaultReasonPhrase;

    SipStatusClass(int min, int max, String defaultReasonPhrase) {
        this.min = min;
        this.max = max;
        this.defaultReasonPhrase = AsciiString.cached(defaultReasonPhrase);
    }

    /**
     * Returns {@code true} if and only if the specified HTTP status code falls into this class.
     */
    public boolean contains(int code) {
        return code >= min && code < max;
    }

    /**
     * Returns the default reason phrase of this HTTP status class.
     */
    AsciiString defaultReasonPhrase() {
        return defaultReasonPhrase;
    }
}

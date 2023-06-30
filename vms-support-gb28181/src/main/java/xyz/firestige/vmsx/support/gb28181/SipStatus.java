package xyz.firestige.vmsx.support.gb28181;

import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Optional;

/**
 * SipStatus
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/18
 **/
public enum SipStatus implements SipStatusCode {
    /**
     * 100
     */
    Trying(100, Series.INFORMATIONAL, "Trying");

    public static SipStatus valueOf(int statusCode) {
        return Optional.ofNullable(resolve(statusCode))
                .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + statusCode + "]"));
    }

    @Nullable
    public static SipStatus resolve(int statusCode) {
        return Arrays.stream(values())
                .filter(status -> status.value() == statusCode)
                .findFirst()
                .orElse(null);
    }

    private final int code;
    private final String reasonPhrase;
    private final Series series;

    SipStatus(int code, Series series, String reasonPhrase) {
        this.code = code;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public int value() {
        return code;
    }

    public Series series() {
        return series;
    }

    public String reasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public boolean is1xx() {
        return false;
    }

    @Override
    public boolean is2xx() {
        return false;
    }

    @Override
    public boolean is3xx() {
        return false;
    }

    @Override
    public boolean is4xx() {
        return false;
    }

    @Override
    public boolean is5xx() {
        return false;
    }

    @Override
    public boolean is6xx() {
        return false;
    }

    @Override
    public boolean isError() {
        return false;
    }

    public enum Series {
        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Series(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static Series valueOf(int statusCode) {
            return Optional.ofNullable(resolve(statusCode))
                    .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + statusCode + "]"));
        }

        @Nullable
        public static Series resolve(int statusCode) {
            return Arrays.stream(values())
                    .filter(series -> series.value == statusCode)
                    .findFirst()
                    .orElse(null);
        }
    }
}

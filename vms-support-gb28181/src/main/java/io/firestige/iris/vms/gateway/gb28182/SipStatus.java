package io.firestige.iris.vms.gateway.gb28182;

import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Optional;

public enum SipStatus implements SipStatusCode {
    //todo 补齐所有状态码
    ;

    private static final SipStatus[] VALUES;
    static {
        VALUES = values();
    }

    private final int value;
    private final Series series;
    private final String reasonPhrase;

    SipStatus(int value, Series series, String reasonPhrase) {
        this.value = value;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public int value() {
        return value;
    }

    public Series series() {
        return series;
    }

    public String getReasonPhrase() {
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

    @Override
    public String toString() {
        return this.value + " " + name();
    }

    public static SipStatus valueOf(int statusCode) {
        return Optional.ofNullable(resolve(statusCode))
                .orElseThrow(() -> new IllegalArgumentException("No matching constant for [" + statusCode + "]"));
    }

    @Nullable
    public static SipStatus resolve(int statusCode) {
        return Arrays.stream(VALUES).filter(status -> status.value == statusCode).findFirst().orElse(null);
    }


    public enum Series {
        ;
        private final int value;

        Series(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Series valueOf(int statusCode) {
            Series series = resolve(statusCode);
            if (series == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            }
            return series;
        }

        public static Series resolve(int statusCode) {
            int seriesCode = statusCode / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return series;
                }
            }
            return null;
        }
    }
}

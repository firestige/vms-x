package io.firestige.iris.vms.support.gb28181;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Optional;

/**
 * SipStatusCode
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/18
 **/
public sealed interface SipStatusCode extends Serializable permits DefaultSipStatusCode, SipStatus {
    int value();
    boolean is1xx();
    boolean is2xx();
    boolean is3xx();
    boolean  is4xx();
    boolean is5xx();
    boolean is6xx();
    boolean isError();
    default boolean isSameCodeAs(SipStatusCode other) {
        return value() == other.value();
    }

    static SipStatusCode  valueOf(int code) {
        Assert.isTrue(code >= 100 && code <= 999,
                () -> "Status code '" + code + "' should be a three-digit positive integer");
        return Optional.<SipStatusCode>ofNullable(SipStatus.resolve(code))
                .orElseGet(() -> new DefaultSipStatusCode(code));
    }
}

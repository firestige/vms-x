package io.firestige.iris.vms.gateway.gb28182;

import java.io.Serial;
import java.io.Serializable;

final class DefaultSipStatusCode implements SipStatusCode, Comparable<SipStatusCode>, Serializable {
    @Serial
    private static final long serialVersionUID = 4202457525587197258L;
    private final int value;

    public DefaultSipStatusCode(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public boolean is1xx() {
        return hundreds() == 1;
    }

    @Override
    public boolean is2xx() {
        return hundreds() == 2;
    }

    @Override
    public boolean is3xx() {
        return hundreds() == 3;
    }

    @Override
    public boolean is4xx() {
        return hundreds() == 4;
    }

    @Override
    public boolean is5xx() {
        return hundreds() == 5;
    }

    @Override
    public boolean is6xx() {
        return hundreds() == 6;
    }

    @Override
    public boolean isError() {
        int hundreds = hundreds();
        return hundreds > 3;
    }

    private int hundreds() {
        return this.value / 100;
    }

    @Override
    public int compareTo(SipStatusCode o) {
        return Integer.compare(value, o.value());
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof SipStatusCode that && this.value == that.value());
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

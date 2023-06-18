package io.firestige.iris.vms.support.gb28181;

import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * DefaultSipStatusCode
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/18
 **/
record DefaultSipStatusCode(int value) implements SipStatusCode, Comparable<SipStatusCode>, Serializable {
    @Serial
    private static final long serialVersionUID = -5948841408989831766L;

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
        return hundreds() > 3;
    }

    private int hundreds() {
        return this.value / 100;
    }

    @Override
    public int compareTo(@NonNull final SipStatusCode other) {
        return Integer.compare(this.value, other.value());
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof SipStatusCode that && this.value == that.value());
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}

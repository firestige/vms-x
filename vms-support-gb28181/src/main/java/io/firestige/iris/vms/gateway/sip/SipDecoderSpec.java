package io.firestige.iris.vms.gateway.sip;

import java.util.function.Supplier;

public abstract class SipDecoderSpec<T extends SipDecoderSpec<T>> implements Supplier<T> {
    public static final int DEFAULT_MAX_INITIAL_LINE_LENGTH             = 4096;
    public static final int DEFAULT_MAX_HEADER_SIZE                     = 8192;
    public static final boolean DEFAULT_VALIDATE_HEADERS                = true;
    public static final int DEFAULT_INITIAL_BUFFER_SIZE                 = 128;

    protected int maxInitialLineLength             = DEFAULT_MAX_INITIAL_LINE_LENGTH;
    protected int maxHeaderSize                    = DEFAULT_MAX_HEADER_SIZE;
    protected boolean validateHeaders              = DEFAULT_VALIDATE_HEADERS;
    protected int initialBufferSize                = DEFAULT_INITIAL_BUFFER_SIZE;

    public T maxInitialLineLength(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("maxInitialLineLength must be strictly positive");
        }
        this.maxInitialLineLength = value;
        return get();
    }

    public int maxInitialLineLength() {
        return maxInitialLineLength;
    }

    public T maxHeaderSize(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("maxHeaderSize must be strictly positive");
        }
        this.maxHeaderSize = value;
        return get();
    }

    public int maxHeaderSize() {
        return maxHeaderSize;
    }

    public T validateHeaders(boolean validate) {
        this.validateHeaders = validate;
        return get();
    }

    public boolean validateHeaders() {
        return validateHeaders;
    }

    public T initialBufferSize(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("initialBufferSize must be strictly positive");
        }
        this.initialBufferSize = value;
        return get();
    }

    public int initialBufferSize() {
        return initialBufferSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SipDecoderSpec<?> that)) {
            return false;
        }
        return maxInitialLineLength == that.maxInitialLineLength &&
                maxHeaderSize == that.maxHeaderSize &&
                validateHeaders == that.validateHeaders &&
                initialBufferSize == that.initialBufferSize;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + maxInitialLineLength;
        result = 31 * result + maxHeaderSize;
        result = 31 * result + Boolean.hashCode(validateHeaders);
        result = 31 * result + initialBufferSize;
        return result;
    }
}

package io.firestige.iris.vms.gateway.gb28182.server.reactive;

import io.firestige.iris.vms.gateway.sip.ng.spring.SipMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.net.URI;

public abstract class AbstractServerSipRequest implements ServerSipRequest {
    private final URI uri;
    private final SipHeaders headers;
    @Nullable
    private final SipMethod method;
    @Nullable
    private String id;
    @Nullable
    private String logPrefix;

    public AbstractServerSipRequest(SipMethod method, URI uri, MultiValueMap<String, String> headers) {
        Assert.notNull(method, "Method must not be null");
        Assert.notNull(uri, "Uri must not be null");
        Assert.notNull(headers, "Headers must not be null");
        this.method = method;
        this.uri = uri;
        this.headers = SipHeaders.readOnlySipHeaders(headers);
    }

    @Override
    public String getId() {
        if (this.id == null) {
            this.id = initId();
            if (this.id == null) {
                this.id = ObjectUtils.getIdentityHexString(this);
            }
        }
        return this.id;
    }

    @Nullable
    protected String initId() {
        return null;
    }

    @Override
    public SipMethod getMethod() {
        // TODO: remove null check once deprecated constructors have been removed
        if (this.method != null) {
            return this.method;
        } else {
            throw new IllegalStateException("No SipMethod provided in constructor, " +
                    "and AbstractServerHttpRequest::getMethod not overridden");
        }
    }

    @Override
    public URI getURI() {
        return this.uri;
    }

    @Override
    public SipHeaders getHeaders() {
        return headers;
    }

    public abstract <T> T getNativeRequest();

    String getLogPrefix() {
        if (this.logPrefix == null) {
            this.logPrefix = "[" + initLogPrefix() + "] ";
        }
        return this.logPrefix;
    }

    protected String initLogPrefix() {
        return getId();
    }
}

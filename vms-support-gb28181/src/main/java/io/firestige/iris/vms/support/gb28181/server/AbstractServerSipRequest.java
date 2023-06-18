package io.firestige.iris.vms.support.gb28181.server;

import io.firestige.iris.vms.support.gb28181.SipHeaders;
import io.firestige.iris.vms.support.gb28181.SipMethod;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

import java.net.URI;
import java.util.Optional;

/**
 * AbstractServerSipRequest
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/14
 **/
public abstract class AbstractServerSipRequest implements ServerSipRequest {
    // todo 待完善
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
        this.headers = SipHeaders.readOnlyHttpHeaders(headers);
    }

    @Override
    public String getId() {
        return Optional.ofNullable(this.id).orElseGet(() -> {
                    this.id = this.initId().orElseGet(() -> ObjectUtils.getIdentityHexString(this));
                    return this.id;
                });
    }

    @NonNull
    protected Optional<String> initId() {
        return Optional.empty();
    }

    @Override
    public SipMethod getMethod() {
        return this.method;
    }

    @Override
    public URI getUri() {
        return this.uri;
    }

    @Override
    public SipHeaders getHeaders() {
        return this.headers;
    }

    public abstract <T> T getNativeRequest();

    String getLogPrefix() {
        return Optional.ofNullable(this.logPrefix).orElseGet(() -> {
            this.logPrefix = "[" + initLogPrefix() + "] ";
            return this.logPrefix;
        });
    }

    protected String initLogPrefix() {
        return getId();
    }
}

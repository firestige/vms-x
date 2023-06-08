package io.firestige.iris.vms.gateway.gb28182.server.reactive;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SipHeaders implements MultiValueMap<String, String>, Serializable {
    public static final SipHeaders EMPTY = new ReadOnlySipHeaders(new LinkedMultiValueMap<>());
    public static SipHeaders readOnlySipHeaders(MultiValueMap<String, String> headers) {
        return switch (headers) {
            case null -> throw new IllegalArgumentException("SipHeaders must not be null");
            case ReadOnlySipHeaders readOnlySipHeaders -> readOnlySipHeaders;
            case SipHeaders sipHeaders -> new ReadOnlySipHeaders(sipHeaders.headers);
            default -> new ReadOnlySipHeaders(headers);
        };
    }

    public static SipHeaders writableSipHeaders(SipHeaders headers) {
        Assert.notNull(headers, "SipHeaders must not be null");
        if (headers == EMPTY) {
            return new SipHeaders();
        }
        return headers instanceof ReadOnlySipHeaders ? new SipHeaders(headers.headers) : headers;
    }

    public static String formatHeaders(MultiValueMap<String, String> headers) {
        return headers.entrySet().stream()
                .map(entry -> {
                    List<String> values = entry.getValue();
                    return entry.getKey() + ":" + (values.size() == 1 ?
                            "\"" + values.get(0) + "\"" :
                            values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(", ")));
                })
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static String encodeBasicAuth(String username, String password, @Nullable Charset charset) {
        Assert.notNull(username, "Username must not be null");
        Assert.doesNotContain(username, ":", "Username must not contain a colon");
        Assert.notNull(password, "Password must not be null");
        if (charset == null) {
            charset = StandardCharsets.ISO_8859_1;
        }

        CharsetEncoder encoder = charset.newEncoder();
        if (!encoder.canEncode(username) || !encoder.canEncode(password)) {
            throw new IllegalArgumentException(
                    "Username or password contains characters that cannot be encoded to " + charset.displayName());
        }

        String credentialsString = username + ":" + password;
        byte[] encodedBytes = Base64.getEncoder().encode(credentialsString.getBytes(charset));
        return new String(encodedBytes, charset);
    }
    final MultiValueMap<String, String> headers;

    public SipHeaders() {
        this(CollectionUtils.toMultiValueMap(new LinkedCaseInsensitiveMap<>(8, Locale.ENGLISH)));
    }

    public SipHeaders(MultiValueMap<String, String> headers) {
        Assert.notNull(headers, "MultiValueMap must not be null");
        this.headers = headers;
    }

    @Override
    public String getFirst(String key) {
        return null;
    }

    @Override
    public void add(String key, String value) {

    }

    @Override
    public void addAll(String key, List<? extends String> values) {

    }

    @Override
    public void addAll(MultiValueMap<String, String> values) {

    }

    @Override
    public void set(String key, String value) {

    }

    @Override
    public void setAll(Map<String, String> values) {

    }

    @Override
    public Map<String, String> toSingleValueMap() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public List<String> get(Object key) {
        return null;
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return null;
    }

    @Override
    public List<String> remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<List<String>> values() {
        return null;
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return null;
    }
}

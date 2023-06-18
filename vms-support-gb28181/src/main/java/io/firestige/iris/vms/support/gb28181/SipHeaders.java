package io.firestige.iris.vms.support.gb28181;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SipHeaders
 *
 * @author firestige
 * @version 0.1.0
 * @createAt 2023/6/16
 **/
public class SipHeaders implements MultiValueMap<String, String>, Serializable {
    @Serial
    private static final long serialVersionUID = 6917933410596968767L;

    private static final SipHeaders sipHeaders = new SipHeaders();

    /**
     * According to Development Engineering, this is not a bug; Prior to 1.5 it was really only possible to implement
     * class literals using Class.forName. The implementation all the way back to 1.2, when it was introduced,
     * operates in exactly this way. So it's not possible for us to change this behaviour in 1.4.2 without breaking
     * compatibility since it possible that users of class literals are relying on the initialization. In 1.5 because
     * of changes in the VM spec it's possible to implement it in a way which would avoid the deadlock in this
     * particular case but that requires compiling and running with a 1.5 or later JDK. It's always possible to write
     * code for static initializers which deadlocks and this is just another instance of it. They should refactor
     * their initialization to avoid this deadlock either by making it more lazy or by eliminating the initialization
     * cycle by adding a separate class. We can't change these semantics in 1.4.2.
     * <p>
     * seems not a problem in JDK 17
     */
    public static final SipHeaders EMPTY = new ReadOnlySipHeaders(new LinkedMultiValueMap<>());

    public static SipHeaders readOnlyHttpHeaders(MultiValueMap<String, String> headers) {
        return (headers instanceof SipHeaders httpHeaders ? readOnlyHttpHeaders(httpHeaders) :
                new ReadOnlySipHeaders(headers));
    }

    public static SipHeaders readOnlyHttpHeaders(SipHeaders headers) {
        Assert.notNull(headers, "HttpHeaders must not be null");
        return (headers instanceof ReadOnlySipHeaders ? headers : new ReadOnlySipHeaders(headers.headers));
    }

    public static SipHeaders writableSipHeaders(SipHeaders headers) {
        Assert.notNull(headers, "HttpHeaders must not be null");
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

    final MultiValueMap<String, String> headers;

    public SipHeaders() {
        this(CollectionUtils.toMultiValueMap(new LinkedCaseInsensitiveMap<>(8, Locale.ENGLISH)));
    }

    public SipHeaders(MultiValueMap<String, String> headers) {
        Assert.notNull(headers, "MultiValueMap must not be null");
        this.headers = headers;
    }

    @Override
    @Nullable
    public String getFirst(@NonNull String key) {
        return this.headers.getFirst(key);
    }

    @Override
    public void add(@NonNull String key, String value) {
        this.headers.add(key, value);
    }

    @Override
    public void addAll(@NonNull String key, @NonNull List<? extends String> values) {
        this.headers.addAll(key, values);
    }

    @Override
    public void addAll(@NonNull MultiValueMap<String, String> values) {
        this.headers.addAll(values);
    }

    @Override
    public void set(@NonNull String key, String value) {
        this.headers.set(key, value);
    }

    @Override
    public void setAll(@NonNull Map<String, String> values) {
        this.headers.setAll(values);
    }

    @Override
    public Map<String, String> toSingleValueMap() {
        return this.headers.toSingleValueMap();
    }

    @Override
    public int size() {
        return this.headers.size();
    }

    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return this.headers.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return this.headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return this.headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {
        this.headers.putAll(m);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return this.headers.values();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    public void clearContentHeaders() {
        //todo 待补齐相关header
        this.headers.remove("");
    }
}

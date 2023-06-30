package xyz.firestige.vmsx.support.gb28181.server;

import io.netty.handler.codec.http.HttpHeaders;
import xyz.firestige.vmsx.support.gb28181.SipHeaders;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * NettyHeadersAdapter
 *
 * @author firestige
 * @createAt 2023/6/18
 **/
public class NettyHeadersAdapter implements MultiValueMap<String, String> {
    private final HttpHeaders headers;
    public NettyHeadersAdapter(HttpHeaders httpHeaders) {
        this.headers = httpHeaders;
    }

    @Override
    public String getFirst(@NonNull String key) {
        return this.headers.get(key);
    }

    @Override
    public void add(@NonNull String key, String value) {
        Optional.ofNullable(value).ifPresent(v -> this.headers.add(key, v));
    }

    @Override
    public void addAll(@NonNull String key, @NonNull List<? extends String> values) {
        this.headers.add(key, values);
    }

    @Override
    public void addAll(@NonNull MultiValueMap<String, String> values) {
        values.forEach(this.headers::add);
    }

    @Override
    public void set(@NonNull String key, String value) {
        Optional.ofNullable(value).ifPresent(v -> this.headers.set(key, v));
    }

    @Override
    public void setAll(@NonNull Map<String, String> values) {
        values.forEach(this.headers::set);
    }

    @Override
    @NonNull
    public Map<String, String> toSingleValueMap() {
        Map<String, String> singleValueMap = CollectionUtils.newLinkedHashMap(this.headers.size());
        this.headers.entries()
                .forEach(entry -> {
                    if (!singleValueMap.containsKey(entry.getKey())) {
                        singleValueMap.put(entry.getKey(), entry.getValue());
                    }
                });
        return singleValueMap;
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
        return key instanceof String name && this.headers.contains(name);
    }

    @Override
    public boolean containsValue(Object value) {
        return value instanceof String
                && this.headers.entries().stream().anyMatch(entry -> entry.getValue().equals(value));
    }

    @Override
    public List<String> get(Object key) {
        return containsKey(key) ? this.headers.getAll((String) key) : null;
    }

    @Override
    public List<String> put(String key, List<String> value) {
        List<String> previousValues = this.headers.getAll(key);
        this.headers.set(key, value);
        return previousValues;
    }

    @Override
    public List<String> remove(Object key) {
        if (key instanceof String headerName) {
            List<String> previousValues = this.headers.getAll(headerName);
            this.headers.remove(headerName);
            return previousValues;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> map) {
        map.forEach(this.headers::set);
    }

    @Override
    public void clear() {
        this.headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return new HeaderNames();
    }

    @Override
    public Collection<List<String>> values() {
        return this.headers.names().stream()
                .map(this.headers::getAll).toList();
    }

    @Override
    public Set<Entry<String, List<String>>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<String, List<String>>> iterator() {
                return new EntryIterator();
            }

            @Override
            public int size() {
                return headers.size();
            }
        };
    }

    @Override
    public String toString() {
        return SipHeaders.formatHeaders(this);
    }

    private class EntryIterator implements Iterator<Entry<String, List<String>>> {

        private final Iterator<String> names = headers.names().iterator();

        @Override
        public boolean hasNext() {
            return this.names.hasNext();
        }

        @Override
        public Entry<String, List<String>> next() {
            return new HeaderEntry(this.names.next());
        }
    }

    private class HeaderEntry implements Entry<String, List<String>> {

        private final String key;

        HeaderEntry(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public List<String> getValue() {
            return headers.getAll(this.key);
        }

        @Override
        public List<String> setValue(List<String> value) {
            List<String> previousValues = headers.getAll(this.key);
            headers.set(this.key, value);
            return previousValues;
        }
    }

    private class HeaderNames extends AbstractSet<String> {

        @Override
        public Iterator<String> iterator() {
            return new HeaderNamesIterator(headers.names().iterator());
        }

        @Override
        public int size() {
            return headers.names().size();
        }
    }

    private final class HeaderNamesIterator implements Iterator<String> {

        private final Iterator<String> iterator;

        @Nullable
        private String currentName;

        private HeaderNamesIterator(Iterator<String> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        @Override
        public String next() {
            this.currentName = this.iterator.next();
            return this.currentName;
        }

        @Override
        public void remove() {
            if (this.currentName == null) {
                throw new IllegalStateException("No current Header in iterator");
            }
            if (!headers.contains(this.currentName)) {
                throw new IllegalStateException("Header not present: " + this.currentName);
            }
            headers.remove(this.currentName);
        }
    }
}

package net.javac.buffer;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.javac.Javac;
import org.jspecify.annotations.NonNull;

public class BufferRegistry<K, V> {
    protected final Cache<@NonNull K, V> buffer;

    public BufferRegistry(int max) {
        if (max <= 0) throw new IllegalArgumentException("max must be greater than 0.");
        this.buffer = Caffeine.newBuilder().maximumSize(max).build();
    }

    public void append(K key, V value) {
        buffer.put(key, value);
    }

    public V get(K key) {
        return buffer.getIfPresent(key);
    }
}

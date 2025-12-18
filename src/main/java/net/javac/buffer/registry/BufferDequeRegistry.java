package net.javac.buffer.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jspecify.annotations.NonNull;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;

public class BufferDequeRegistry<K, V> implements BufferRegistryPolicy<K, Deque<V>> {
    protected final Cache<@NonNull K, Deque<V>> buffer;

    private Supplier<? extends Deque<V>> dequeFactory = ConcurrentLinkedDeque::new;

    public BufferDequeRegistry(int max) {
        if (max <= 0) throw new IllegalArgumentException("max must be greater than 0.");
        this.buffer = Caffeine.newBuilder().maximumSize(max).build();
    }

    @Override
    public Deque<V> get(K key) {
        return null;
    }

    @Override
    public void append(K key, Deque<V> value) {
        Deque<V> dq = get(key);

        if (dq == null) {
            dq = getInstance();
            buffer.put(key, dq);
        }

        dq.addAll(value);
    }

    public Deque<V> getInstance() {
        return dequeFactory.get();
    }

    public void setInstance(Supplier<? extends Deque<V>> instance) {
        this.dequeFactory = instance;
    }

}

package net.javac.buffer;

public abstract class AbstractBuffer<K, V> {
    protected final BufferRegistry<K, V> registry;

    public AbstractBuffer(int max) {
        this.registry = new BufferRegistry<>(max);
    }

    public abstract V get(K key);

    public abstract void append(K key, V value);

    public abstract String getName();
}

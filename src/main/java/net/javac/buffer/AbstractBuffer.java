package net.javac.buffer;

import net.javac.buffer.registry.BufferRegistryPolicy;

public abstract class AbstractBuffer<K, V> {
    protected final BufferRegistryPolicy<K, V> registry;

    public AbstractBuffer(BufferRegistryPolicy<K, V> registry) { this.registry = registry; }

    public abstract V get(K key);

    public abstract void append(K key, V value);

    public abstract String getName();
}

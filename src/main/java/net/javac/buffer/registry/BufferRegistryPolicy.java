package net.javac.buffer.registry;

public interface BufferRegistryPolicy <K, V> {
    V get(K key);
    void append(K key, V value);
}

package net.javac.buffer;

public interface Buffer<K, V> {
    V get(K key);
    void append(K key, V val);
}

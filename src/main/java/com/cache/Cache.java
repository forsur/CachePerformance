package com.cache;

public interface Cache<K, V> {
    void put(K key, V value);
    V get(K key);

    int getSize();

    void reset();
}

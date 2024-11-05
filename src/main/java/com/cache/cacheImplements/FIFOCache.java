package com.cache.cacheImplements;

import com.cache.Cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FIFOCache<K, V> implements Cache<K, V> {
    private int capacity;
    private Map<K, V> buffer;

    public FIFOCache(int capacity)
    {
        this.capacity = capacity;
        // 设置第三个参数为 false ，表示按插入顺序维护 LinkedHashMap
        buffer = Collections.synchronizedMap(new LinkedHashMap<K, V>(capacity, 0.75f, false){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
            {
                return size() > capacity;
            }
        });
    }

    @Override
    public V get(K key)
    {
        V ret = buffer.get(key);
        if(ret == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("thread interrupted", e);
            }
            V dataFromMemory = (V) (Integer)0;
            buffer.put(key, dataFromMemory);
        }
        return ret;
    }

    @Override
    public void put(K key, V value)
    {
        buffer.put(key, value);
    }


    @Override
    public int getSize()
    {
        return buffer.size();
    }


    @Override
    public void reset()
    {
        buffer.clear();
    }
}

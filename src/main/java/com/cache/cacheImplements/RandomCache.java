package com.cache.cacheImplements;

import com.cache.Cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class RandomCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private Map<K, V> buffer;
    private Random random;

    public RandomCache(int capacity)
    {
        this.capacity = capacity;
        // 使用父类判定方式，不主动移除元素
        buffer = Collections.synchronizedMap(new LinkedHashMap<K, V>(capacity));
        random = new Random();
    }

    @Override
    public V get(K key)
    {
        V ret = buffer.get(key);
        if(ret == null)
        {
            try {
                Thread.sleep(100);
            }catch(InterruptedException e) {
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
        if(buffer.size() >= capacity)
        {
            // 通过随机取数组索引随机选一个 key
            K randomKey = (K) buffer.keySet().toArray(new Object[0])[random.nextInt(capacity)];
            buffer.remove(randomKey);
        }
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

package com.cache.cacheImplements;

import com.cache.Cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {
    private int capacity;
    private Map<K, V> buffer;

    // 构造函数
    public LRUCache(int capacity)
    {
        this.capacity = capacity;
        // 保证线程同步
        // 设置第三个参数为 true ，表示按访问(get/put)顺序维护 LinkedHashMap
        this.buffer = Collections.synchronizedMap(new LinkedHashMap<K, V>(capacity, 0.75f, true){
            // 在匿名内部类中重写 替换方法 ，当容量达到最大值时才移除元素
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                // 每次插入元素时调用，判断是否移除元素
                return size() > capacity;
            }
        });
    }

    /**
     * 1.cache hit：当找到需要的元素时，直接读出；
     * 2.cache miss：当需要的元素不存在时，首先sleep一段时间，用于模拟从内存中取数据的时间
     * 随后调用 put 方法插入该元素
     */
    @Override
    public V get(K key)
    {
        V ret = buffer.get(key);
        if(ret == null){
            // buffer 是线程安全的，所以不会阻塞其他线程
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

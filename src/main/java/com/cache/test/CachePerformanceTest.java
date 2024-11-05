package com.cache.test;

import com.cache.Cache;
import com.cache.cacheImplements.FIFOCache;
import com.cache.cacheImplements.LRUCache;
import com.cache.cacheImplements.RandomCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CachePerformanceTest {

    private static void performanceTest(Cache<Integer, Integer>cache, List<DataGenerator.KeyValuePair<Integer,Integer>>data, String name, int threadCount) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        AtomicInteger hitCount = new AtomicInteger(); // 保证操作的原子性

        System.out.println("Testing " + name + " Cache...");

        long startTime = System.currentTimeMillis();

        for(int i=0;i<threadCount;i++)
        {
            // 交给线程池并行执行
            executorService.submit(() -> {
                for(DataGenerator.KeyValuePair<Integer,Integer> pair : data)
                {
                    Integer k = pair.getKey();
                    Integer v = pair.getValue();

                    // 模拟 cache 的读写
                    if(v % 2 == 0){
                        cache.put(k, v);
                    }else{
                        if(cache.get(k) != null){
                            hitCount.incrementAndGet(); // 原子性自增
                        }
                    }
                }
            });
        }

        executorService.shutdown();
        // 等待固定时间
        boolean allDone = executorService.awaitTermination(50, TimeUnit.SECONDS);

        long endTime = System.currentTimeMillis();
        double testTime = (endTime - startTime) / 1_000_000.0; // 以毫秒为单位
        double hitRate = (double) hitCount.get() / (data.size() * threadCount) * 100;

        System.out.println("Elapsed Time: " + testTime + " ms");
        System.out.println("Hit Rate: " + hitRate + "%");
        System.out.println("Number of items in cache: " + cache.getSize());
        System.out.println(allDone ? "All done" : "Thread not done");
        System.out.println(" ");
    }


    /**
     *  main test
     */
    public static void main(String[] args) throws InterruptedException{
        int cacheCapacity = 80;
        int testDataSize = 1000;
        int threadCount = 8;
        // 模拟 GPU 数据存取
        int locality = 15;
        int hotKeyCount = 25;

        Cache<Integer, Integer> lruCache = new LRUCache<Integer, Integer>(cacheCapacity);
        Cache<Integer, Integer> fifoCache = new FIFOCache<Integer, Integer>(cacheCapacity);
        Cache<Integer, Integer> randomCache = new RandomCache<Integer, Integer>(cacheCapacity);

        List<DataGenerator.KeyValuePair<Integer, Integer>> testData = DataGenerator.generateData(testDataSize, locality, hotKeyCount);

        performanceTest(lruCache, testData, "LRU", threadCount);
        performanceTest(fifoCache, testData, "FIFO", threadCount);
        performanceTest(randomCache, testData, "Random", threadCount);
    }


}

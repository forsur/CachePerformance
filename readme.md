# CachePerformance

> 现代多核处理器和多线程技术使得多个 GPU 核心可以同时访问缓存。
>
> 所以在GPU cache中，使用了多线程来模拟并发访问缓存。

控制变量：为了模拟大量的 cache 访问情况，对于三种替换策略，这里均通过使用线程安全的 `LinkedHashMap` 来实现 cache ，从而使得替换和查找操作的平均时间复杂度均为 $O(1)$ 。对于 cache miss 的情况，则使用 `Thread.sleep()` 来模拟从内存取数据写入 cache 的延迟。



根据 GPU 访存 cache 的特性设置的测试数据生成算法如下：

```java
// 生成热键（频繁访问的键）
List<Integer> hotKeys = new ArrayList<>(hotKeyCount);
for (int i = 0; i < hotKeyCount; i++) {
    hotKeys.add(random.nextInt(locality));
}

for (int i = 0; i < size; i++) {
    int key;
    if (random.nextDouble() < 0.2) { // 20%的概率选择热键
        key = hotKeys.get(random.nextInt(hotKeyCount));
    } else {
        key = (i / locality) * locality + random.nextInt(locality);
    }
    int value = random.nextInt(1000);
    data.add(new KeyValuePair<>(key, value));
}
```


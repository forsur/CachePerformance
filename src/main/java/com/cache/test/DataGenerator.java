package com.cache.test;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    public static List<KeyValuePair<Integer, Integer>> generateData(int size, int locality, int hotKeyCount) {
        List<KeyValuePair<Integer, Integer>> data = new ArrayList<>(size);
        Random random = new Random();

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

        return data;
    }

    @Data
    public static class KeyValuePair<K, V> {
        private final K key;
        private final V value;

        public KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }


}
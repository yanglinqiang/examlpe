package com.ylq.example.cache;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

/**
 * Class description goes here
 * Created by ylq on 16/5/24 上午11:35.
 */
public class Chronicle {
    protected static ChronicleMap<Integer, Boolean> chronicleMapCache = ChronicleMapBuilder
            .of(Integer.class, Boolean.class)
            .constantKeySizeBySample(0)
            .constantValueSizeBySample(true)
            .entries(10000000)
            .create();

    public static void main(String[] args) {
        System.out.println("start");
        Long start = System.currentTimeMillis();
        for (int i = 0; i < 21; i++) {
            chronicleMapCache.put(i, i % 3 == 0);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            for (int j = 0; j < i; j++) {
                System.out.printf(chronicleMapCache.get(i).toString());
            }
            System.out.println("");
        }
//        for (int i = 10; i < 15; i++) {
//            chronicleMapCache.put(i, i % 3 == 0);
//        }
        System.out.println("ok");
        System.out.println("耗时(毫秒)：" + (System.currentTimeMillis() - start));
    }
}

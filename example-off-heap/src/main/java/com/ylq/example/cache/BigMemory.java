package com.ylq.example.cache;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class description goes here
 * Created by ylq on 16/5/23 上午11:05.
 */
public class BigMemory {

    /**
     * jdk1.8(-Xmx4g -Xms4g)
     * ok
     * 耗时(毫秒)：40754
     * 占用系统内存：2.81G
     * 堆内存 1.9G
     */
    protected static Map<String, Boolean> cache = new ConcurrentHashMap<>();


    /**
     * (3.0.0-6M)(jdk1.8)(-Xmx4g -Xms4g)
     * ok
     * 耗时(毫秒)：96647
     * 占用系统内存：2.47G
     * 堆内 ：1.28G
     */
    protected static DB db = DBMaker.memoryDirectDB().make();
    protected static HTreeMap<String, Boolean> HTreeMapCache = db.hashMap("test")
            .keySerializer(Serializer.STRING)
            .valueSerializer(Serializer.BOOLEAN)
            .expireMaxSize(10000000)
            .create();


    /**
     * ChronicleMap3 只支持java8
     * ok
     * 耗时(毫秒)：49218
     * 占用系统内存：1.83G
     * 堆内 ：30M
     */
    protected static ChronicleMap<String, Boolean> chronicleMapCache = ChronicleMapBuilder
            .of(String.class, Boolean.class)
            .constantKeySizeBySample(UUID.randomUUID().toString())
            .constantValueSizeBySample(true)
            .entries(10000000)
            .create();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("start");
        Long start = System.currentTimeMillis();
        // 大概3个G左右
        for (int i = 0; i < 10000000; i++) {
//            cache.put(UUID.randomUUID().toString(), i % 3 == 0);
            HTreeMapCache.put(UUID.randomUUID().toString(), i % 3 == 0);
//            chronicleMapCache.put(UUID.randomUUID().toString(), i % 3 == 0);
        }
        System.out.println("ok");
        System.out.println("耗时(毫秒)：" + (System.currentTimeMillis() - start));
        Thread.sleep(1000 * 60 * 60);
    }
}

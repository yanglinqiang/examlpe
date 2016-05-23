package com.ylq.example.cache;


import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.UUID;

/**
 * Class description goes here
 * Created by ylq on 16/5/23 上午11:05.
 */
public class BigMemory {

    //    ok
    //    耗时(毫秒)：41956
    //    占用系统内存：2.77G,压缩后1.76G
    //    堆内存 2.25G
//    protected static Map<String, Boolean> cache = new ConcurrentHashMap<>();


    //        ok(1.0.9)
    //        耗时(毫秒)：187431
    //        占用系统内存：2.34G，压缩后1.43G
    //        堆内 ：610M



    //    ok(3.0.0-6M)
    //    耗时(毫秒)：93077
    //    占用系统内存：1.72G，压缩后33.8M
    //    堆内 ：1.1G
    protected static DB db = DBMaker.memoryDB().make();
    protected static HTreeMap<String, Boolean> cache = db.hashMap("test").keySerializer(Serializer.STRING).valueSerializer(Serializer.BOOLEAN).expireMaxSize(10000000).create();


//    /**
//     * ChronicleMap3 只支持java8
//     * ChronicleMap2  支持java7 但是超出数量时回报异常错误
//     */
//    protected static ChronicleMap<String, Boolean> cache = ChronicleMapBuilder
//            .of(String.class, Boolean.class)
//            .averageKey("Amsterdam")
//            .entries(1000L)
//            .create();

    public static void main(String[] args) throws InterruptedException {
        Long start = System.currentTimeMillis();
        // 大概3个G左右
        for (int i = 0; i < 10000000; i++) {
            cache.put(UUID.randomUUID().toString(), i / 3 == 0);
        }
        System.out.println("ok");
        System.out.println("耗时(毫秒)：" + (System.currentTimeMillis() - start));
        Thread.sleep(1000 * 60 * 60);
    }
}

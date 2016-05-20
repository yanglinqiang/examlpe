package com.ylq.example.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class description goes here
 * Created by ylq on 16/4/15 下午12:24.
 */
public class Practice {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            res.add(i);
        }
        Iterator<Integer> integers = res.iterator();
        while (integers.hasNext()) {
            Integer item = integers.next();
            if (item % 3 == 0) {
                integers.remove();
            }
        }
        for (Integer re : res) {
            System.out.println(re);
        }
        System.out.println(System.currentTimeMillis());
    }
}

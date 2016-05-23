package com.ylq.example.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description goes here
 * Created by ylq on 16/5/23 上午11:05.
 */
public class BigMemory {

    private static List<int[]> temp = new ArrayList<int[]>();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 70000000; i++) {
            temp.add(new int[10000]);
        }

        Thread.sleep(1000 * 60 * 60);
    }
}

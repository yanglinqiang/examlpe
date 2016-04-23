package com.ylq.example.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description goes here
 * Created by ylq on 16/4/15 下午12:24.
 */
public class Practice {
    public static void main(String[] args) throws InterruptedException {
        List<Long> res = new ArrayList<>();
        res.add(0l);
        res.add(1l);
        for (int i = 0; i < 64; i++) {
            res.add(res.get(res.size() - 2) + res.get(res.size() - 1));
        }

        for (Long re : res) {
            System.out.println(re);
        }
    }
}

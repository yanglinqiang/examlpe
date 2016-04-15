package com.ylq.example.common;

import java.util.Arrays;

/**
 * Class description goes here
 * Created by ylq on 16/4/15 下午12:24.
 */
public class Practice {
    public static void main(String[] args) {
        Integer[] a = {1, 2, 3};
        Integer[] b = Arrays.copyOf(a, 6);
        System.arraycopy(a,0,b,3,3);
        for (int i = 0; i < b.length; i++) {
            System.out.println(b[i]);
        }

    }
}

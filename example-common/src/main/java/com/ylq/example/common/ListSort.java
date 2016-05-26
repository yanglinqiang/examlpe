package com.ylq.example.common;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Class description goes here
 * Created by ylq on 16/5/25 下午7:17.
 */
public class ListSort {
    public static void main(String[] args) {
        List<String> strings = new ArrayList();
        for (int i = 0; i < 50; i++) {
            strings.add(UUID.randomUUID().toString());
        }
        Collections.sort(strings);

        for (String string : strings) {

            System.out.println(string);
        }
    }
}

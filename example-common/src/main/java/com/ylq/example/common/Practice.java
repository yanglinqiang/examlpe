package com.ylq.example.common;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

/**
 * Class description goes here
 * Created by ylq on 16/4/15 下午12:24.
 */
public class Practice {

    public static void main(String[] args) throws IOException {
        System.out.println( substringBeforeLast("aaaa-androiddddd-android", "-android"));
    }

}

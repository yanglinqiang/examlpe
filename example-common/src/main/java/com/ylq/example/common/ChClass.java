package com.ylq.example.common;

/**
 * Created by 杨林强 on 16/6/17.
 */
public class ChClass extends BaseClass {
    @Override
    protected void test1(){
        super.test1();
        System.out.println('c');
    }

    public static void main(String[] args) {
        new ChClass().test1();
    }
}

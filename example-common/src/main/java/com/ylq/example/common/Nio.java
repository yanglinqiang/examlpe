package com.ylq.example.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Class description goes here
 * Created by ylq on 16/4/23 下午5:57.
 */
public class Nio {
    public static void main(String[] args) {
        Nio nio = new Nio();
        nio.testByteBuffer();
    }

    public void testByteBuffer() {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("/Users/ylq/tem.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 获取通道
        FileChannel fc = fin.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 使用allocateDirect，而不是allocate
//        ByteBuffer buffer = ByteBuffer.allocateDirect( 1024 );

//        byte array[] = new byte[1024];
//        ByteBuffer buffer2 = ByteBuffer.wrap( array );

        // 读取数据到缓冲区
        try {
            fc.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        buffer.flip();
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.print(((char) b));
        }
    }
}

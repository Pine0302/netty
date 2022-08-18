package com.pine.nio;

import java.nio.IntBuffer;

import java.nio.channels.Channel;

public class BasicBuffer {
    public static void main(String[] args) {

        //举例说明buffer的作用

        //创建一个buffer 大小为5 ，可以创建5个 int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向buffer存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }

        //从buffer中读取数据
        //将buffer转换，读写切换
        intBuffer.flip();

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}

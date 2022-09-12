package com.pine.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * mappedByteBuffer 可以让文件直接映射到内存(堆外内存)中去修改，操作系统不需要拷贝一次
 */
public class MappedBufferTest06 {
    public static void main(String[] args) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");
        //获取对应的文件通道
        FileChannel channel = randomAccessFile.getChannel();

        System.out.println("randomAccessFile 的长度："+randomAccessFile.length());

        /**
         * 1.参数1 FileChannel.MapMode.READ_WRITE 使用读写模式
         * 2.参数2 0：可以直接修改的起始位置
         * 3.参数3 5：映射到内存的大小（即将1.txt的多少个字节映射到内存）
         * 可以直接修改的范围是0-4
         * mappedByteBuffer 实际类型是DirectByteBuffer
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) 'P');
        mappedByteBuffer.put(1,(byte) 'I');
        mappedByteBuffer.put(2,(byte) 'N');
        mappedByteBuffer.put(3,(byte) 'E');
        mappedByteBuffer.put(4,(byte) 'Q');


        randomAccessFile.close();


    }

}

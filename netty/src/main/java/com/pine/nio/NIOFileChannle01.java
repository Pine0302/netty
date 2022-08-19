package com.pine.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannle01 {
    public static void main(String[] args) throws IOException {

        String text = "hello pine2";

        //创建一个输出流 channel
        File file = new File("NIOFileChannle01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        //通过 fileOutputStream 获取对应的fileChannel
        //这个 fileChannel 真实的数据类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将string 放入 byteBuffer
        byteBuffer.put(text.getBytes());

        //对byteBuffer进行反转
        byteBuffer.flip();

        //将byteBuffer数据写入fileChannel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();


    }
}

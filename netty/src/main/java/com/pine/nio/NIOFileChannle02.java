package com.pine.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannle02 {
    public static void main(String[] args) throws IOException {

        //创建一个输出流 channel
        File file = new File("NIOFileChannle01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过 fileInputStream 获取对应的fileChannel
        //这个 fileChannel 真实的数据类型是 FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将channel数据读入buffer
        fileChannel.read(byteBuffer);

        fileInputStream.close();

        //对byteBuffer进行反转
        byteBuffer.flip();

        String string2 = new String(byteBuffer.array(), 0, byteBuffer.limit());
        System.out.println(string2);



    }
}

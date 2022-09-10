package com.pine.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannle04 {
    public static void main(String[] args) throws IOException {

        //创建一个输出流 channel
        File fileInput = new File("1.jpg");
        FileInputStream fileInputStream = new FileInputStream(fileInput);
        FileChannel inputChannel = fileInputStream.getChannel();


        //创建一个输出流 channel
        File fileOutput = new File("2.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
        FileChannel outputChannel = fileOutputStream.getChannel();

        //channel 复制
        outputChannel.transferFrom(inputChannel,0, inputChannel.size());

        outputChannel.close();
        inputChannel.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}

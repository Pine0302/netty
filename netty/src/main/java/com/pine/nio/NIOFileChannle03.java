package com.pine.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannle03 {
    public static void main(String[] args) throws IOException {

        //创建一个输出流 channel
        File fileInput = new File("1.txt");
        FileInputStream fileInputStream = new FileInputStream(fileInput);
        FileChannel inputChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileInput.length()/2);

        //创建一个输出流 channel
        File fileOutput = new File("2.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
        FileChannel outputChannel = fileOutputStream.getChannel();

        while(true){
            //将channel数据读入buffer
            byteBuffer.clear();
            int read = inputChannel.read(byteBuffer);
            if(read==-1){
                break;
            }
            byteBuffer.flip();
            outputChannel.write(byteBuffer);

        }


        fileInputStream.close();
        fileOutputStream.close();




    }
}

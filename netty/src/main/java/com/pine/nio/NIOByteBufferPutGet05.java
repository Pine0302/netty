package com.pine.nio;

import java.io.IOException;
import java.nio.ByteBuffer;

public class NIOByteBufferPutGet05 {
    public static void main(String[] args) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(100);
        buffer.putChar('沈');
        buffer.putLong(10L);
        buffer.putShort((short) 10);

        buffer.flip();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getShort());


        ByteBuffer buffer1 = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            buffer1.put((byte) i);
        }

        //翻转
        buffer1.flip();

        //得到一个只读buffer
        ByteBuffer readOnlyBuffer = buffer1.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer);

        //读取
        while(readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte) 100);   //ReadOnlyBufferException

    }
}

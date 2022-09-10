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



    }
}

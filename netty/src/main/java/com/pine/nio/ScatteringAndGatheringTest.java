package com.pine.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering: 将数据写入buffer，可以采用buffer数组，依次写入
 * Gathering: 从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {

        //使用 ServerSocketChannel 和 SocketChannel网络

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7777);

        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端链接（telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8; //假定从客户端接受8个字节
        //循环的读取
        while (true){

            int byteRead = 0;
            while(byteRead < messageLength){
                Long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteread="+byteRead);

                //使用流打印，查看当前这个buffer的position和limit
                Arrays.asList(byteBuffers).stream().map(buffer->"position="+buffer.position()+"limit="+buffer.limit()).forEach(System.out::println);

                //将所有的buffer进行反转
                Arrays.asList(byteBuffers).forEach(buffer->buffer.flip());

                //将数据读出，显示到客户端
                long byteWrite = 0;
                while (byteWrite < messageLength){
                    long lw = socketChannel.write(byteBuffers);
                    byteWrite += lw;
                }

                //将所有的buffer进行clear
                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

                System.out.println(" byteRead="+byteRead+" bytewirte="+byteWrite+" messagelength="+messageLength);

            }




        }

    }
}

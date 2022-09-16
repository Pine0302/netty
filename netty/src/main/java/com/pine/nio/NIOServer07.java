package com.pine.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer07 {
    public static void main(String[] args) throws IOException {

        //创建serverSocketChannel -> serverSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个selector对象
        Selector selector = Selector.open();

        //绑定一个端口6666 在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel 注册到 selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while(true){

            //这里我们等待1s，如果没有事件发生，就返回继续
            if(selector.select(1000)==0){ //没有事件发生
                System.out.println("服务器等待了1s，无连接");
                continue;
            }

            //如果返回>0,获取到相关的selectionKey集合
            //1.如果返回的>0 ,表示已经获取到关注的事件
            //2. selector.selectedKeys() 返回关注事件的集合
            //3. 通过 selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历 Set<SelectionKey>
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if(key.isAcceptable()){ //如果是OP_ACCEPT事件，有新的客户端连接我
                    //给该客户端客户生成一个 socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel"+socketChannel.hashCode());
                    //将socketchannle设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将当前的socketChannel注册到selector上面，关注事件 OP_READ，同时给socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(512));
                }
                if(key.isReadable()){ //如果是 OP_READ 事件，有新的客户端连接我
                    //通过key 反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();

                    channel.read(byteBuffer);
                    System.out.println("from 客户端 " + new String(byteBuffer.array()));
                }

                //手动从集合中移除当前的selectionKey，防止重复操作
                keyIterator.remove();
            }

        }


    }
}

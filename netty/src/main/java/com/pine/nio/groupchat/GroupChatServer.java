package com.pine.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器初始化
    public GroupChatServer(){

        try{
            //得到选择器
            selector = Selector.open();

            //ServerSocketChannel
            listenChannel = ServerSocketChannel.open();

            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));

            //设置非阻塞模式
            listenChannel.configureBlocking(false);

            //将该 listenChannel 注册到 selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //监听
    public void listen(){

        System.out.println("监听线程： "+Thread.currentThread().getName());
        try{
            //循环处理
            while (true){

                int count = selector.select();
                if(count > 0){ //有事件处理
                    System.out.println("开始监听");
                    //遍历得到的selectionKey集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()){
                        //取出selectionKey
                        SelectionKey key = keyIterator.next();

                        //监听到 accept
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();

                            sc.configureBlocking(false);
                            //将该 sc 注册到 selector 上面
                            sc.register(selector,SelectionKey.OP_READ);

                            //提示上线
                            System.out.println(sc.getRemoteAddress() + " 上线了。。。");
                        }

                        if(key.isReadable()){ //通道发生 read 事件 即通道是可读的状态
                            //读信息
                            readData(key);
                        }

                        keyIterator.remove();
                    }

                }else {
                    System.out.println("等待。。。。");
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    //读取客户端消息
    private void readData(SelectionKey key)  {

        //定义一个socketChannel
        SocketChannel channel = null;
        try {
            //取到关联的channel
            channel = (SocketChannel) key.channel();
            //创建缓冲 buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int count = channel.read(byteBuffer);

            //根据 count 的值 ，做处理

            if(count > 0){
                //把缓存区的数据转成字符串
                String msg = new String(byteBuffer.array());
                //输出该消息
                System.out.println("from 客户端： " + channel.getRemoteAddress() + "-" + msg);

                //向其他客户端转发消息(去掉 自己->发消息的客户端 ),专门一个方法处理
                sendInfoToOtherClients(msg,channel);
            }

        }catch (IOException e){
            try{
                System.out.println(channel.getRemoteAddress() + " 离线了。。。");
                //取消注册
                key.channel();
                //关闭通道
                channel.close();
            }catch (IOException e2){
                e2.printStackTrace();
            }


        }

    }

    //转发消息给其他客户（通道）
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException {

        System.out.println("服务器转发数据给客户端线程： "+Thread.currentThread().getName());
        System.out.println("服务器转发消息中。。。");

        //遍历所有注册到 selector 上的 socketChannel 并排除 self
        for (SelectionKey key:selector.keys()){
            //通过 key 取出对应的 socketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self){

                //转型
                SocketChannel dest = (SocketChannel) targetChannel;

                //将 msg 存储到 buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer 的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {

        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();


    }




}

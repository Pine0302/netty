package com.pine.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient1 {
    //定义属性
    private final String HOST = "127.0.0.1";
    private static final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;


    //构造器初始化
    public GroupChatClient1(){

        try{
            //得到选择器
            selector = Selector.open();

            //连接服务器
            socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));

            socketChannel.configureBlocking(false);

            socketChannel.register(selector,SelectionKey.OP_READ);

            username = socketChannel.getLocalAddress().toString().substring(1);

            System.out.println(username + " is ok ....");

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //向服务器发送消息
    public void sendInfo(String info) {
        info  = username + " 说：" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //读取从服务器端恢复的消息
    public void readInfo(){
        try{
            int readChannels = selector.select();
            if(readChannels > 0){  //有可用的通道（有事件发生的通道）
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        //得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读取
                        sc.read(buffer);
                        //把读到的缓存区的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }

                }
                iterator.remove();
            }else {
                System.out.println("没有可用的通道");
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //启动客户端
        GroupChatClient1 chatClient = new GroupChatClient1();

        //启动一个线程 每隔三秒 读取从服务器端可能发送的数据
        new Thread(){
            public void run(){

                while(true){
                    chatClient.readInfo();
                    try{
                        Thread.currentThread().sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            chatClient.sendInfo(s);

        }


    }



}

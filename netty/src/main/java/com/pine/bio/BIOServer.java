package com.pine.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String args[]) throws IOException {

        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程，与之通讯
        ExecutorService pool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        while(true){
            //监听，等待客户端链接
            System.out.println("线程信息 id= "+ Thread.currentThread().getId()+" 线程名称="+Thread.currentThread().getName()+" ");
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端了");
            pool.execute(new Runnable(){

                //就创建一个线程，与之通讯
                public void run() {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }

    }

    //handle方法与客户端通讯
    public static void handler(Socket socket) throws IOException {
        try{
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while(true){
                System.out.println("线程信息 id= "+ Thread.currentThread().getId()+" 线程名称="+Thread.currentThread().getName()+" ");
                System.out.println("read...........");
                int read = inputStream.read(bytes);
                if(read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            socket.close();
        }


    }
}

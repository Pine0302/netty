package com.pine.netty.heartbeat;

import com.pine.netty.groupchat.GroupChatServerhandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {

    private int port; //监听端口

    public MyServer(int port) {
        this.port = port;
    }

    //编写 run 方法 ，处理客户端的请求
    public void run() throws InterruptedException {


        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))  //在bossGroup增加日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到 pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个 netty 提供的 IdleStateHandler
                            /**
                             * 说明
                             * 1. IdleStateHandler 是 netty 提供的处理空闲状态的处理器
                             * 2.long readerIdleTime,表示多久时间没有读，就会发送一个心跳检测包，检测是否还是链接的状态
                             * 3.long writerIdleTime,表示多久时间没有写，就会发送一个心跳检测包，检测是否还是链接的状态
                             * 4.long allIdleTime,表示多久时间既没有读也没有写，就会发送一个心跳检测包，检测是否还是链接的状态
                             * 5.文档说明
                             * 6.当 IdleStateEvent 触发后，就会传递给管道的下一个 handler 去处理
                             * 通过调用（触发） 下一个handler 的 userEventTriggered，在该方法中处理IdleStateEvent（读空闲，写空闲，读写空闲）
                             */
                            pipeline.addLast(new IdleStateHandler(13,5,2, TimeUnit.SECONDS));

                            //加入一个队空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            //监听关闭时间
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws InterruptedException {
        new MyServer(3020).run();
    }

}

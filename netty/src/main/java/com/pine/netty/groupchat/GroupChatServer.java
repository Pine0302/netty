package com.pine.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {

    private int port; //监听端口

    public GroupChatServer(int port) {
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
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //获取到 pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            //向 pipeline 里加入一个解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //向 pipeline 里加入一个编码器
                            pipeline.addLast("encode", new StringEncoder());
                            //加入自己的业务处理 handler
                            pipeline.addLast(new GroupChatServerhandler());
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
        new GroupChatServer(7000).run();
    }

}

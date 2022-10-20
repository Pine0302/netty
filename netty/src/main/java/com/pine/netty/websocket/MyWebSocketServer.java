package com.pine.netty.websocket;

import com.pine.netty.groupchat.GroupChatServerhandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class MyWebSocketServer {
    public static void main(String[] args) throws InterruptedException {

        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            //获取到 pipeline
                            ChannelPipeline pipeline = ch.pipeline();

                            //因为是基于http 协议
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加 ChunkedWriteHandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * 1.因为http 数据在传输过程中是分段的，HttpObjectAggregator 就是可以将多个段聚合起来
                             * 2. 这就是为什么，当浏览器发送大量数据时，就会发出多次 http 请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 1.对于 websocket ，他的数据是以 帧（frame） 形式传递
                             * 2.可以看到 WebSocketFrame 下面有6个子类
                             * 3.浏览器请求时 ws://localhost:7000/xxx 表示请求的uri
                             * 4.WebSocketServerProtocolHandler 核心功能是将 http 协议升级为 ws 协议，保持长链接
                             * 5. 是通过一个 状态码 101 来实现的
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //加入自己的业务处理 handler
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(3028).sync();

            //监听关闭时间
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

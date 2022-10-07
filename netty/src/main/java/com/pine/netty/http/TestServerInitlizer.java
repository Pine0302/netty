package com.pine.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitlizer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入 netty 提供的 httpServerCodec =>[code - decode]
        //1.HttpServerCodec 说明： HttpServerCodec 是 netty 提供的 http 编码/解码 器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //2.增加自己自定义的handdler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());
    }
}

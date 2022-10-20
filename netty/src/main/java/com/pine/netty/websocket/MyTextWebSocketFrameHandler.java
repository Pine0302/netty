package com.pine.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;
import java.util.Date;

//这里 TextWebSocketFrame 类型 ，表示一个文本帧（frame）
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间："+ LocalDateTime.now()+" "+msg.text()));
    }


    //handlerAdded 表示连接建立，一旦连接，第一个被执行
    // 将当前 channel 加入到 channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // id 表示唯一的值 ，longText 是唯一的 ShortText不是唯一的
        System.out.println("handlerAdded 被调用" +  channel.id().asLongText());
        System.out.println("handlerAdded 被调用" +  channel.id().asShortText());
    }


    //端口连接，将xx 客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // id 表示唯一的值 ，longText 是唯一的 ShortText不是唯一的
        System.out.println("handlerRemoved 被调用" +  channel.id().asLongText());
        System.out.println("handlerRemoved 被调用" +  channel.id().asShortText());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        System.out.println("异常发生" +  cause.getMessage());
        //关闭通
        ctx.close();
    }



}

package com.pine.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 说明
 * 1.我们自定义一个 handler 需要继承 netty 规定好的某个 HandlerAdapter
 * 2.这时我们自定义一个 Handler ，才能成为一个 handler
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {



    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client "+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello，server:喵~~",CharsetUtil.UTF_8));
    }


    //读取数据事件（这里我们可以读取客户端发送的消息）
    /**
     * 1.当通道有读取事件时，会触发
     * 2. Object msg：客户端发送的数据 默认是Object
     *
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息："+ buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器端地址："+ ctx.channel().remoteAddress());
    }

    //处理异常 - 关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}

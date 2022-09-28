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

    //读取数据事件（这里我们可以读取客户端发送的消息）
    /**
     * 1.ChannelHandlerContext ctx: 上下文对象，含有 管道 pipeline ，通道 channel ，地址
     * 2. Object msg：客户端发送的数据 默认是Object
     *
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = "+ ctx);
        // 将 msg 转成一个  ByteBuf
        //ByteBuf 是 netty 提供的 ，不是 NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是："+ buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ ctx.channel().remoteAddress());
    }


    /**
     * 数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //writeAndFlush 是 write + flush 将数据写入到缓存里 并刷新
        // 一般，我们对发送的 数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , 客户端~",CharsetUtil.UTF_8));
    }

    //处理异常 - 关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}

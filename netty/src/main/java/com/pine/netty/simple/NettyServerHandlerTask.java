package com.pine.netty.simple;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明
 * 1.我们自定义一个 handler 需要继承 netty 规定好的某个 HandlerAdapter
 * 2.这时我们自定义一个 Handler ，才能成为一个 handler
 */
public class NettyServerHandlerTask extends ChannelInboundHandlerAdapter {

    //读取数据事件（这里我们可以读取客户端发送的消息）
    /**
     * 1.ChannelHandlerContext ctx: 上下文对象，含有 管道 pipeline ，通道 channel ，地址
     * 2. Object msg：客户端发送的数据 默认是Object
     *
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //如果这里我们的业务很耗时间 --> 异步执行 --> 提交到该 channel 对应的 NIOEventLoop 的 taskQueue 中
        // 解决方案1 用户程序自定义的普通任务

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello , 客户端~喵2",CharsetUtil.UTF_8));
            }
        });
        System.out.println("go on");


        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello , 客户端~喵3",CharsetUtil.UTF_8));
            }
        });
        System.out.println("go on");

        // 解决方案1 用户程序自定义的定时任务，该任务是提交到 schduleTaskQueue 中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello , 客户端~喵4",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            },5, TimeUnit.SECONDS);

        /*Thread.sleep(10*1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , 客户端~喵2",CharsetUtil.UTF_8));*/
    }


    /**
     * 数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //writeAndFlush 是 write + flush 将数据写入到缓存里 并刷新
        // 一般，我们对发送的 数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , 客户端喵1~",CharsetUtil.UTF_8));
    }

    //处理异常 - 关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }




}

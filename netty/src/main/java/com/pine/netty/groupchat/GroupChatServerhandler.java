package com.pine.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerhandler extends SimpleChannelInboundHandler<String> {


    //定义一个 channel 组 ，管理所有的 channel
    // GlobalEventExecutor.INSTANCE 是一个全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //handlerAdded 表示连接建立，一旦连接，第一个被执行
    // 将当前 channel 加入到 channelGroup
    @Override
    public void  handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客加入聊天的信息推送给其他在线的客户端
        //该方法会将 channelGroup 中所有的channel 遍历，并发送消息
        channelGroup.writeAndFlush( sdf.format(new Date()) +" [客户端] " + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);
    }

    //表示 channel 处于活动状态，提示 xx 上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }

    //表示 channel 处于非活动状态，提示 xx 离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    //端口连接，将xx 客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
        System.out.println("channelGroup size：" + channelGroup.size());
    }


    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        //获取到当前 channel
        Channel channel = ctx.channel();

        //遍历 channelGroup, 根据不同的情况，回送不同消息
        channelGroup.forEach(ch->{
            if(channel != ch) { //不是当前的 channel,转发消息
                ch.writeAndFlush(" [客户] "+channel.remoteAddress() + " 发送消息： "+ msg + "\n");
            }else{
                ch.writeAndFlush("[自己] 发送了消息： " + msg + "\n");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        //关闭通道
        ctx.close();
    }


}

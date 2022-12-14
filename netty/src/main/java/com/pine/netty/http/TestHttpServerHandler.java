package com.pine.netty.http;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明
 * 1.SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类
 * 2.HttpObject 表示 客户端和服务器端相互通讯的数据被封装成 httpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // channelRead0 ： 读取客户端
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        // 判断 msg 是不是 HttpRequest 请求
        if( msg instanceof HttpRequest){

            System.out.println("pipeline hashcode= "+ ctx.pipeline().hashCode() + "  TestHttpServerHandler hash = "+this.hashCode());

            System.out.println("msg 类型=" + msg.getClass());
            System.out.println("客户端地址： "+ ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico ,不做响应");
                return;
            }

            //回复信息给浏览器

            ByteBuf content = Unpooled.copiedBuffer("hello , 我是你爸爸", CharsetUtil.UTF_16);

            //构造 http 响应 ，即 httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //将构建好的 response 返回
            ctx.writeAndFlush(response);


        }
    }
}

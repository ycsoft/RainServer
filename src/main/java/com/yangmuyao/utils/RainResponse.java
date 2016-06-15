package com.yangmuyao.utils;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;

/**
 * Created by fengyuyangchen on 16/6/14.
 */
public class RainResponse {


    public static void  ResponseOk(MessageEvent e){
        HttpResponse    resp = new DefaultHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK);
        String          str = "{\"result\":\"OK\"}";
        ChannelBuffer   buf = new DynamicChannelBuffer(64);

        resp.headers().add("Content-Type","application/json");
        resp.headers().add("Content-Length",str.length());

        buf.writeBytes(str.getBytes());

        resp.setContent(buf);

        ChannelFuture future =  e.getChannel().write(resp);
        future.addListener(ChannelFutureListener.CLOSE);


    }

    public static void ResponseFail(MessageEvent e){
        HttpResponse    resp = new DefaultHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.FORBIDDEN);
        String          str = "{\"result\":\"Fail\"}";
        ChannelBuffer   buf = new DynamicChannelBuffer(64);

        resp.headers().add("Content-Type","application/json");
        resp.headers().add("Content-Length",str.length());

        buf.writeBytes(str.getBytes());

        resp.setContent(buf);

        ChannelFuture future =  e.getChannel().write(resp);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}

package com.yangmuyao.router;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.marshalling.ThreadLocalMarshallerProvider;
import sun.misc.BASE64Decoder;

import java.util.List;
import java.util.Map;


/**
 * Created by fengyuyangchen on 16/6/12.
 */
public class EventRouter implements URLRouter {

    public            Logger  log = Logger.getLogger(EventRouter.class);

    public void handleRequest(Map<String,List<String>>  params, HttpRequest req, MessageEvent e) throws Exception{

        String                 value = null;
        int                     pos  = 0;
        BASE64Decoder           b64  = new BASE64Decoder();
        ChannelBuffer           buf = new DynamicChannelBuffer(16);
        ChannelFuture           future = null;

        if ( params.size() <= 0 ){

            HttpResponse  res = new DefaultHttpResponse(HttpVersion.HTTP_1_0,HttpResponseStatus.FORBIDDEN);
            future = e.getChannel().write(res);

        }else{
            List<String>    lsvalue = params.get("param");
            if ( lsvalue != null && lsvalue.size() > 0){
                value = lsvalue.get(0);
                value = new String(b64.decodeBuffer(value));
                log.debug("base64 decode result:" + value);
                HttpResponse resp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                resp.setContent(buf);
                future = e.getChannel().write(resp);
            }

        }

        future.addListener(ChannelFutureListener.CLOSE);
    }

    public String toString(){
        return "com.yangmuyao.router.EventRouter";
    }


}

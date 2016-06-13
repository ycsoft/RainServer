package com.yangmuyao.router;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by fengyuyangchen on 16/6/12.
 */
public interface URLRouter {
    public void handleRequest(Map<String,List<String>> params, HttpRequest req, MessageEvent e) throws Exception;
}

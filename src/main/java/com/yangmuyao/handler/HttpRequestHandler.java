package com.yangmuyao.handler;

import com.yangmuyao.RouterConfig;
import com.yangmuyao.router.URLRouter;
import com.yangmuyao.utils.RainResponse;
import com.yangmuyao.utils.UrlUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.util.CharsetUtil;

import java.util.*;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Values;

/**
 * Created by fengyuyangchen on 16/6/12.
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler{

    private HttpRequest                 request;
    private boolean                     readingChunks   = false;
    private final StringBuilder         buf             = new StringBuilder();
    private static Logger               log             = Logger.getLogger(HttpRequestHandler.class);
    private static RouterConfig         routerConfig    = new RouterConfig();


    public HttpRequestHandler(){
        super();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception{
        if (!readingChunks){

            HttpRequest request = this.request = (HttpRequest)e.getMessage();
            String uri = UrlUtils.getPath( request.getUri() );

            //解析请求参数
            Map<String, List<String>> params = null;
            if ( request.getMethod() == HttpMethod.GET){
                params = httpGetParams(request);
            }else{
                params = httpPostParams(request);
            }
            //分发URL请求
            URLRouter  router = routerConfig.getRouters().get(uri);
            if ( router != null ){
                router.handleRequest(params,request,e);
            }else{
                log.error("Can not get Router");
                RainResponse.ResponseFail(e);
            }
            //解析HTTP头
//            for (Map.Entry<String, String> h: request.headers()){
//                System.out.println("Header: " + h.getKey() + " = " + h.getValue() );
//            }


//            if ( !params.isEmpty()){
//                for (Map.Entry<String,List<String>> p: params.entrySet()){
//                    String key = p.getKey();
//                    List<String> vals = p.getValue();
//                    for ( String val:vals){
//                        System.out.println("PARAM:" + key + " = " + val + "\r\n");
//                    }
//                }
//            }

            if ( request.isChunked()){
                readingChunks = true;
            }else{
                ChannelBuffer   content = request.getContent();
                if ( content.readable()){
                    //buf.append("CONTENT:" + content.toString(CharsetUtil.UTF_8) + "\r\n");
                }
                //writeResponse(e);
            }

        }else{
            HttpChunk chunk = (HttpChunk)e.getMessage();
            if ( chunk.isLast()){
                readingChunks = false;
                //END OF CONTENT
                buf.append("END OF CONTENT\r\n");
                HttpChunkTrailer trailer = (HttpChunkTrailer)chunk;
                if ( !trailer.trailingHeaders().names().isEmpty()){
                    buf.append("\r\n");
                    for ( String name:trailer.trailingHeaders().names()){
                        for( String value: trailer.trailingHeaders().getAll(name)){
                            buf.append("TRAILING HEADER: "+name + " = " + value + "\r\n");
                        }
                    }
                    buf.append("\r\n");
                }
                writeResponse(e);
            }else{
                buf.append("CHUNK: " + chunk.getContent().toString(CharsetUtil.UTF_8) + "\r\n");
            }
        }
    }

    private  void writeResponse( MessageEvent e){
        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        response.setContent(ChannelBuffers.copiedBuffer(buf.toString(),CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE,"text/plain;charset=UTF-8");
        if (keepAlive){
            response.headers().set(CONTENT_LENGTH,response.getContent().readableBytes());
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
        }
        //Encode the cookie
        String cookieString =  request.headers().get(COOKIE);
        if ( cookieString != null ){
            CookieDecoder cookieDecoder = new CookieDecoder();
            Set<Cookie> cookies = cookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()){
                CookieEncoder  cookieEncoder  = new CookieEncoder(true);
                for (Cookie cookie : cookies){
                    cookieEncoder.addCookie(cookie);
                    response.headers().add(SET_COOKIE,cookieEncoder.encode());
                }
            }
        }else{
            //Browser sent no cookie, Add some
//            CookieEncoder cookieEncoder = new CookieEncoder(true);
//            cookieEncoder.addCookie("key1","value1");
//            response.headers().add(SET_COOKIE,cookieEncoder.encode());
//            cookieEncoder.addCookie("key2","value2");
//            response.headers().add(SET_COOKIE,cookieEncoder.encode());
        }
        //write the response
        ChannelFuture future =  e.getChannel().write(response);
        if (!keepAlive){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }


    public void exceptionCaught(ChannelHandlerContext ctx,ExceptionEvent e){
        e.getCause().printStackTrace();
        e.getChannel().close();
    }

    public static Map<String,List<String>>  httpGetParams(HttpRequest request){
        QueryStringDecoder queryStringDecoder =  new QueryStringDecoder(request.getUri());
        Map<String, List<String>> params = queryStringDecoder.getParameters();

        return params;
    }

    public static Map<String,List<String>> httpPostParams(HttpRequest request) throws Exception{

        Map<String,List<String>>         post = new HashMap<String,List<String>>();

        HttpPostRequestDecoder        decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false),request);
        List<InterfaceHttpData>      postdata = decoder.getBodyHttpDatas();

        int count = postdata.size();
        if ( postdata == null || count <= 0 ){
            return post;
        }else{
            for ( InterfaceHttpData data: postdata){
                List<String> ls = new ArrayList<String>();
                String key      =  data.getName();
                String value    = data.toString();
                int    pos      = value.indexOf("=");
                ls.add(value.substring(pos+1));
                post.put(key,ls);
            }
        }

        return post;
    }


}




































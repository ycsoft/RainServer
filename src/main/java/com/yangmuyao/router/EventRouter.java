package com.yangmuyao.router;

import com.yangmuyao.utils.CacheFile;
import com.yangmuyao.utils.FilePool;
import com.yangmuyao.utils.RainResponse;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;
import sun.misc.BASE64Decoder;

import java.util.List;
import java.util.Map;


/**
 * Created by fengyuyangchen on 16/6/12.
 */
public class EventRouter implements URLRouter {

    public             Logger  log = Logger.getLogger(EventRouter.class);

    /**
     *
     * @param params
     * @param req
     * @param e
     * @throws Exception
     */
    public  void handleRequest(Map<String,List<String>>  params, HttpRequest req, MessageEvent e) throws Exception{

        String                  value   = null;
        BASE64Decoder           b64     = new BASE64Decoder();
        long                  threadid  =  Thread.currentThread().getId();

        if ( params.size() <= 0 ){

            RainResponse.ResponseFail(e);

        }else{
            List<String>    lsvalue = params.get("param");
            if ( lsvalue != null && lsvalue.size() > 0){

                value = lsvalue.get(0);
                value = new String(b64.decodeBuffer(value));
                value =  value + "\n";

                FilePool.getFile(threadid).write(value.getBytes());

                RainResponse.ResponseOk(e);
            }

        }

    }

    public String toString(){
        return "com.yangmuyao.router.EventRouter";
    }


}

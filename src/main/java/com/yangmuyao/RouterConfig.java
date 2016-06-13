package com.yangmuyao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yangmuyao.router.URLRouter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by fengyuyangchen on 16/6/12.
 * CopyRight(c)   XD.Yang,2016
 *
 */
public class RouterConfig {

    private Logger log = Logger.getLogger(RouterConfig.class);

    private  Map<String, URLRouter>   routers;

    public    RouterConfig(){
        routers = new HashMap<String, URLRouter>();
    }

    public Map<String,URLRouter>    getRouters(){
        return routers;
    }

    public void setAction(String url, URLRouter router){
        routers.put(url, router);
    }

    public URLRouter getAction(String url){
        return routers.get(url);
    }

    /**
     *
     * 读取配置文件,从文件中加载指定URL对应的处理器
     *
     * @param filename      :配置文件路径
     * @throws Exception
     */
    public  void loadConfig(String filename) throws  Exception{

        File  f             = new File(filename);
        byte [] buf         = null;
        String  contents    = null, value = null;

        if ( !routers.isEmpty()){
            routers.clear();
        }

        if ( !f.exists() ){
            log.warn("router configure file not exists, please check");
            return;
        }

        buf = new byte[(int)f.length()];
        FileInputStream fis = new FileInputStream(f);
        fis.read(buf);

        contents = new String(buf);
        if ( contents.length() < 6 ){
            log.warn("no router setted");
            return;
        }

        JSONObject obj = JSON.parseObject(contents);
        Set<String> keys = obj.keySet();

        for ( String key:keys ){
            value = obj.getString(key);
//            log.debug("Key:"+key+"  Class Name:" + value);
            routers.put(key,(URLRouter)Class.forName(value).newInstance());
        }

    }

}

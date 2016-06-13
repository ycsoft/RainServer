package com.yangmuyao.utils;

/**
 * Created by fengyuyangchen on 16/6/13.
 */
public class UrlUtils {
    /**
     * Check wether the uri correct or not
     * @param uri
     * @return
     */
    public static boolean checkUri(String uri){
        if ( uri.charAt(0) != '/'){
            return false;
        }else{
            return true;
        }
    }
    public static String getPath(String uri){

        int       pos       = uri.indexOf("?");

        if ( !checkUri(uri) ){
            return null;
        }

        if ( pos > 0 ){
            return uri.substring(0,pos);
        }else{
            return uri;
        }

    }
}

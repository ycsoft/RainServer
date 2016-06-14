package com.yangmuyao.cachefile;

import sun.misc.Queue;

/**
 * Created by fengyuyangchen on 16/6/13.
 */

public class CacheFile implements Runnable{

    public volatile Queue<String>  params;

    /***
     * 向队列中添加新数据
     * @param value
     */
    public void push(String value){

    }
    public void run(){

    }

}

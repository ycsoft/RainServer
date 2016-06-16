package com.yangmuyao.utils;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.TimerTask;

/**
 * Created by fengyuyangchen on 16/6/16.
 */
public class CacheTask extends TimerTask{

    /**
     * 定时执行的任务,缓存文件列表
     */
    public void run(){
        try {
            ObjectOutputStream objectout = new ObjectOutputStream(new FileOutputStream(Constant.CacheFile));
            objectout.writeObject( FilePool.fileHistory );
            objectout.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

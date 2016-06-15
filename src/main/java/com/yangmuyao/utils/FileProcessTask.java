package com.yangmuyao.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by fengyuyangchen on 16/6/14.
 */
public class FileProcessTask extends TimerTask {

    public static Logger log = Logger.getLogger(FileProcessTask.class);

    public  static void deleteFile( String filename ){
        Process process = null;
        String   cmd = "rm -fr " + filename;

        try{
            process = Runtime.getRuntime().exec(cmd);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    public void run(){

        System.out.println("Task Running");
        Map<String,CacheFile> cacheFileMap  = FilePool.getAllFiles();
        String newFileName = "tmp/IN---ALL";

        try{
            BufferedOutputStream        bos = new BufferedOutputStream(new FileOutputStream(newFileName,true));
            for( Map.Entry<String,CacheFile> entry: cacheFileMap.entrySet() ){

                String    threadid  = entry.getKey();
                CacheFile cacheFile = cacheFileMap.get( threadid );

                synchronized (cacheFile.lock) {

                    File      f         = new File(cacheFile.filename);
                    if ( !f.exists() || f.length() <= 0 ){
                        continue;
                    }
                    byte    [] bytes    = new byte[ (int)f.length() ];

                    cacheFile.read(bytes);
                    bos.write(bytes);
                    bos.flush();
                    cacheFile.renew();
                    cacheFileMap.remove(threadid);
                    //释放锁以后,其他线程将内容写入新文件
                    cacheFileMap.put(threadid,cacheFile);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

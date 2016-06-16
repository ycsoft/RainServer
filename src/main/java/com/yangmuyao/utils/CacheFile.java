package com.yangmuyao.utils;

import com.yangmuyao.ks3.CloundUpload;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.UUID;

/**
 * Created by fengyuyangchen on 16/6/14.
 */
public class CacheFile {

    public      Object          lock;
    private     int             dataCount   = 0;
    private     static Logger   log         = Logger.getLogger(CacheFile.class);
    /**
     *
     * @param filename
     * @throws Exception
     */
    public CacheFile( String filename ) throws Exception{

        this.filename = filename;
        outstream = new BufferedOutputStream( new FileOutputStream(filename));
        inputStream = new BufferedInputStream( new FileInputStream(filename));

        lock = new Object();
    }
    /**
     *
     * @param bytes
     * @throws Exception
     */
    public  void  write (byte [] bytes) throws Exception{
        synchronized (lock){
            outstream.write(bytes);
            dataCount++;
            if ( dataCount >= Constant.MaxCacheDataCount){
                dataCount = 0;
                outstream.flush(); //写入磁盘,缓存
                outstream.close();
                inputStream.close();
                String oldname = filename;
                //将新来的数据转存
                renew();
                //存储至KS3
                FilePool.executorService.submit(new CloundUpload(oldname));
            }
        }
    }

    /**
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public int read(byte [] bytes) throws Exception{
        synchronized (lock){
            return inputStream.read(bytes);
        }
    }

    /**
     * 删除旧文件,同时新建文件
     * @throws Exception
     */
    public void renew() throws Exception{

        synchronized (lock){
            //生成新的文件名
            this.filename       = "tmp" + File.separator + UUID.randomUUID().toString();
            this.outstream      = new BufferedOutputStream( new FileOutputStream(filename) );
            this.inputStream    = new BufferedInputStream( new FileInputStream( filename ));
            FilePool.fileHistory.add(this.filename);
        }
    }

    String                  barename;
    String                  filename;
    BufferedOutputStream    outstream;
    BufferedInputStream     inputStream;

}
package com.yangmuyao.utils;

import com.sun.deploy.util.SystemUtils;
import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import com.yangmuyao.ks3.KS3Client;
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
                //存储至KS3
                KS3Client   ks3 = new KS3Client(KS3Client.BEIJING);
                ks3.setBucket("citrusjoy.matrix.storage");
                File f = new File(this.filename);
                ks3.putFile("Garfield/session/" + f.getName(),filename);
                //将新来的数据转存
                renew();
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
            inputStream.close();
            outstream.close();
            //删除文件
            File f = new File(this.filename);
            if ( f.isFile() ){
                boolean b = f.delete();
                if (!b ){
                    System.out.println("Delete File Error");
                }
            }

            //生成新的文件名
            this.filename       = "tmp" + File.separator + UUID.randomUUID().toString();

            this.outstream      = new BufferedOutputStream( new FileOutputStream(filename) );
            this.inputStream    = new BufferedInputStream( new FileInputStream( filename ));

        }
    }

    String                  barename;
    String                  filename;
    BufferedOutputStream    outstream;
    BufferedInputStream     inputStream;

}
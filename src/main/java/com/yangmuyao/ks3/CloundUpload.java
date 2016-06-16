package com.yangmuyao.ks3;

import com.yangmuyao.utils.FilePool;

import java.io.File;

/**
 * Created by fengyuyangchen on 16/6/16.
 */



public class CloundUpload implements Runnable {

    private String filename = null;

    /**
     *  在构造函数中指定该线程需要处理的文件
     * @param filename
     */
    public CloundUpload(String filename){

            this.filename = filename;
    }

    /**
     *将文件上传至KS3,如果上传成功,则删除该文件
     */
    public void run(){
        File f = new File(filename);
        KS3Client client = new KS3Client(KS3Client.BEIJING);
        client.setBucket("citrusjoy.matrix.storage");
        if ( client.putFile("Garfield/session/" + f.getName(),this.filename) ){
            f.delete();
            FilePool.fileHistory.remove(filename);
        }
    }
}

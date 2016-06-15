package com.yangmuyao;

import com.yangmuyao.ks3.KS3Client;
import com.yangmuyao.pipeline.HttpServerPipelineFactory;
import com.yangmuyao.utils.FileProcessTask;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import sun.misc.BASE64Encoder;
import java.io.File;

/**
 * Created by fengyuyangchen on 16/6/12.
 */


public class Main {
    public static Logger    log = Logger.getLogger(Main.class);
    /**
     * 检查是否有缓存的,未发送的数据文件,若有,发送之
     * @throws Exception
     */
    public void flushCache() throws Exception{

        ObjectInputStream ins = null;
        try{
            ins  = new ObjectInputStream(new FileInputStream("FileListCache"));
        }catch (Exception e){
            log.warn("No Cache File to Deal with");
            return;
        }

        ConcurrentLinkedQueue<String> filelist = (ConcurrentLinkedQueue<String>)ins.readObject();
        KS3Client       ks3 = null;

        if ( filelist == null || filelist.size() == 0){
            log.info("No cache File to send");
        }else{
            ks3 = new KS3Client(KS3Client.BEIJING);
            ks3.setBucket("citrusjoy.matrix.storage");
        }
        Iterator it  = filelist.iterator();
        while ( it.hasNext() ){

            String value = it.next().toString();
            log.debug("Cache File:" + value);

            File f = new File(value);
            if ( !f.exists() || f.length() == 0 ){
                log.info(value + ":  Skipped");
            }else{
                ks3.putFile(f.getName(),value);
            }
        }
        ins.close();
    }

    public static void main(String [] args) throws  Exception{

        new Main().flushCache();

        ServerBootstrap  bootstrap =  new ServerBootstrap( new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()
        ));

        bootstrap.setPipelineFactory( new HttpServerPipelineFactory());
        bootstrap.bind( new InetSocketAddress(7001));
        Logger log = Logger.getLogger("Main");
        log.info("Server Has Been Started, Please visit http://127.0.0.1:7001/index.html To See It");

        java.util.Timer timer = new Timer();
        timer.schedule( new FileProcessTask(),1000,1000);
    }

}

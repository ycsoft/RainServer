package com.yangmuyao;

import com.yangmuyao.pipeline.HttpServerPipelineFactory;
import com.yangmuyao.utils.FileProcessTask;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.concurrent.Executors;
import sun.misc.BASE64Encoder;
import java.io.File;

/**
 * Created by fengyuyangchen on 16/6/12.
 */


public class Main {


    public static void main(String [] args) throws  Exception{

        ServerBootstrap  bootstrap =  new ServerBootstrap( new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()
        ));

        bootstrap.setPipelineFactory( new HttpServerPipelineFactory());
        bootstrap.bind( new InetSocketAddress(7001));
        Logger log = Logger.getLogger("Main");
        log.info("Server Has Been Started, Please visit http://127.0.0.1:7001/index.html To See It");

//        java.util.Timer timer = new Timer();
//        timer.schedule( new FileProcessTask(),1000,1000);
    }

}

package com.yangmuyao;

import com.yangmuyao.ks3.KS3Client;
import com.yangmuyao.pipeline.HttpServerPipelineFactory;
import com.yangmuyao.router.EventRouter;
import com.yangmuyao.test.RouterConfigTest;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;


import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

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
    }

}

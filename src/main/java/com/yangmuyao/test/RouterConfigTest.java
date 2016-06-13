package com.yangmuyao.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.yangmuyao.RouterConfig;
import com.yangmuyao.router.EventRouter;
import com.yangmuyao.router.URLRouter;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Created by fengyuyangchen on 16/6/13.
 */
public class RouterConfigTest extends TestCase {

    RouterConfig config;

    @Before
    public void setUp(){

        config = new RouterConfig();
        System.out.println("Ready to test......");
    }
    @After
    public void tearDown(){
        System.out.println("Test Complete");
    }
    @Test
    public void testConstruct(){
//        RouterConfig    config =  new RouterConfig();
        Assert.assertNotEquals("Construct Error",config,null);
    }
    @Test
    public void testSetAndGetAction(){
//        RouterConfig    config =  new RouterConfig();
        EventRouter eventRouter = new EventRouter();
        config.setAction("/event",eventRouter);
        Assert.assertEquals("Set and Get action error", eventRouter,config.getAction("/event"));
    }
    @Test
    public void testLoadConfig() throws Exception{
        config.loadConfig("configure.json");
        Map<String,URLRouter>   rts = config.getRouters();
        Assert.assertNotEquals("load config error",rts.size(),0);
    }
    @Test
    public void testURLAction() throws  Exception{
        config.loadConfig("configure.json");
        URLRouter urlrt = config.getRouters().get("/event");
        Assert.assertEquals("Class load error", urlrt.toString(),"com.yangmuyao.router.EventRouter");
    }
}

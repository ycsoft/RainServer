package com.yangmuyao.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import com.yangmuyao.handler.HttpRequestHandler;

/**
 * Created by fengyuyangchen on 16/6/12.
 */
public class HttpServerPipelineFactory implements ChannelPipelineFactory {
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline     pipeline = Channels.pipeline();

        pipeline.addLast("decoder",new HttpRequestDecoder());
        pipeline.addLast("encoder",new HttpResponseEncoder());
        pipeline.addLast("chunkedWriter",new ChunkedWriteHandler());
        pipeline.addLast("deflater", new HttpContentCompressor());
        pipeline.addLast("handler",new HttpRequestHandler());

        return pipeline;
    }
}

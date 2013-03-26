package com.miyue.netty.base;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.miyue.netty.handler.MainServerHandler;

/**
 * 
 * @author spring.tu
 *
 */
public class ServerPipelineFactory implements ChannelPipelineFactory {

	private MainServerHandler mainServerHandler;
	public static double blockingCoefficient = 0.8;
	public static int numberOfCores = Runtime.getRuntime().availableProcessors();
	public static final int poolSize = (int)(numberOfCores / (1 - blockingCoefficient));
	public static final OrderedMemoryAwareThreadPoolExecutor executor= new OrderedMemoryAwareThreadPoolExecutor(poolSize, 0, 0);
	public static final ExecutionHandler executionHandler = new ExecutionHandler(executor); 
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("execution", executionHandler);
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("handler", mainServerHandler);
		return pipeline;
	}

	public void setMainServerHandler(MainServerHandler mainServerHandler) {
		this.mainServerHandler = mainServerHandler;
	}

}
package com.miyue.netty.container;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.miyue.netty.base.ServerPipelineFactory;

/**
 * netty container to start
 * 
 * @author spring.tu
 * 
 */
public class SpringNettyContainer {

	private ServerPipelineFactory serverPipelineFactory;
	private int port;
	private String asePath;
	private static byte[]key;
	private static final Logger logger = Logger.getLogger("container");
	public static ApplicationContext ctx;
	private static String log4jPath = "/usr/miyue/conf/miyue-netty-service-log4j.xml";
	/***** start by spring container's init*********/
	public void init() {
		logger.info("init before the container to start on port " + port + "......");
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(serverPipelineFactory);
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(port));
		/**
		 * ase key's initializing
		 */
		try {
			logger.debug("init key starting......");
			key = FileUtils.readFileToByteArray(new File(asePath));
			logger.debug("init key end......");
		} catch (IOException e) {
			logger.error("read key from asePath error:" + e.getMessage());
		}
		logger.info("init over");
	}

	public void setServerPipelineFactory(
			ServerPipelineFactory serverPipelineFactory) {
		this.serverPipelineFactory = serverPipelineFactory;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public static void main(String[] args) {
		logger.info("app path is:" + System.getProperty("user.dir"));
		DOMConfigurator.configureAndWatch(log4jPath);
		logger.info("###################################");
		logger.info("start nettyContainer....");
		String[] configurations = { 
				"config/applicationContext.xml",
				"config/applicationContext-netty.xml"
		};
		ctx = new ClassPathXmlApplicationContext(configurations);
		logger.info("end to nettyContainer");
		logger.info("###################################");
	}

	public void setAsePath(String asePath) {
		this.asePath = asePath;
	}

	public static byte[] getKey() {
		return key;
	}

	public void setLog4jPath(String log4jPath) {
		this.log4jPath = log4jPath;
	}
}

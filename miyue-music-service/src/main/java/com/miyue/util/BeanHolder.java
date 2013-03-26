package com.miyue.util;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author feize
 *
 */
public class BeanHolder {

	private static ApplicationContext applicationContext;
		static{
		String[] configurations = { 
				"config/applicationContext.xml",
				"config/applicationContext-handler.xml",
				"config/applicationContext-components.xml",
				"config/applicationContext-dao.xml",
				"config/applicationContext-service.xml"
		};
		applicationContext = new ClassPathXmlApplicationContext(configurations);
	}
	
	public static MemcachedClient getMemcacheClient(){
		return (MemcachedClient) applicationContext.getBean("memcachedClient");
	}
}

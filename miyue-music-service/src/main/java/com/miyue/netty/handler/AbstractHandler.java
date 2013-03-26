package com.miyue.netty.handler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;

import com.miyue.netty.vo.HandlerConditionChecker;

public abstract class AbstractHandler {
	protected static final Logger logger = Logger.getLogger("handler");
	protected static final String UPDATE_CODE="0";
	protected static final String NONEUPDATE_CODE="500";
	protected static final String SUCCESS_CODE = "200";
	
	 /**
     * 获取X-Real-IP
     */
    public String getXRealIp(HandlerConditionChecker handlerConditionChecker,DefaultHttpRequest defaultHttpRequest){
    	String ip = null;
    	if (defaultHttpRequest.containsHeader("x-real-ip")) {
			ip = defaultHttpRequest.getHeader("x-real-ip");
		}else{
			ip = handlerConditionChecker.getE().getRemoteAddress().toString();
		}
    	if(StringUtils.isNotEmpty(ip) && ip.startsWith("/")){
    		if(ip.indexOf(":") != -1){
    			ip = StringUtils.substringBetween(ip, "/", ":");
    		}else{    			
    			ip = StringUtils.substring(ip, 1);
    		}
    	}
    	return ip;
    }
	public String getClassName(Class cls){
		return cls.getName();
	}
}

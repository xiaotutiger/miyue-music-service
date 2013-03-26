package com.miyue.netty.vo;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class HandlerConditionChecker {
	private String uriName;
	private String content;
	private byte []contentBytes;
	private MessageEvent e;
	private MiyueTechRequest skyMobiRequest;
	private HttpRequest httpRequest;
	public HandlerConditionChecker(byte[]contentBytes){
		this.contentBytes = contentBytes;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public byte[] getContentBytes() {
		return contentBytes;
	}
	public void setContentBytes(byte[] contentBytes) {
		this.contentBytes = contentBytes;
	}
	public String getUriName() {
		return uriName;
	}
	public void setUriName(String uriName) {
		this.uriName = uriName;
	}
	public MessageEvent getE() {
		return e;
	}
	public void setE(MessageEvent e) {
		this.e = e;
	}
	public MiyueTechRequest getSkyMobiRequest() {
		return skyMobiRequest;
	}
	public void setSkyMobiRequest(MiyueTechRequest skyMobiRequest) {
		this.skyMobiRequest = skyMobiRequest;
	}
	public HttpRequest getHttpRequest() {
		return httpRequest;
	}
	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
}

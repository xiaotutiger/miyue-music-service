package com.miyue.netty.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class MiyueTechRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String host;
	private String url;
	private List<Map.Entry<String, String>> headers;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public List<Map.Entry<String, String>> getHeaders() {
		return headers;
	}
	public void setHeaders(List<Map.Entry<String, String>> headers) {
		this.headers = headers;
	}
}

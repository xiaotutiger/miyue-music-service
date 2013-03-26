package com.miyue.json.request.pojo;

import java.io.Serializable;

public class BaseRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 内部分发请求的optype标识
	 */
	private String optype;

	public String getOptype() {
		return optype;
	}

	public void setOptype(String optype) {
		this.optype = optype;
	}
}

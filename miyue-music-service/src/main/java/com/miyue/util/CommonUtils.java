package com.miyue.util;

import org.apache.commons.codec.digest.DigestUtils;

public class CommonUtils {
	
	/**
	 * @param content
	 * @return
	 */
	public static String genericMd5(String content){
		return DigestUtils.md5(content).toString();
	}
}

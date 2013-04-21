package com.miyue.util;

import com.google.common.base.Optional;

/**
 * google guava工具包
 * @author TWL
 *
 * @param <T>
 */
public class GuavaUtils<T> {
	/**
	 * 判断t类型是否为null
	 * @param t
	 * @return
	 */
	public static <T> boolean isPresent(T t){
		Optional<T>optionalObj = Optional.fromNullable(t);
		return optionalObj.isPresent();
	}
}

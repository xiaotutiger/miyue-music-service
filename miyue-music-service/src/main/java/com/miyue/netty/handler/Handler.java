package com.miyue.netty.handler;

import com.miyue.netty.vo.HandlerConditionChecker;

/**
 * @author spring.tu
 *
 */
public interface Handler {
	/**
	 * default return to json
	 * @param handlerConditionChecker
	 * @return
	 */
	String doHandler(HandlerConditionChecker handlerConditionChecker);
	/**
	 * 获取handler名称
	 * @return
	 */
	String getName();
}

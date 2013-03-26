package com.miyue.util;

import java.util.Map;

/**
 * 用ThreadLocal提供一个存储线程内变量的地方.
 * <p/>
 * 客户端代码可以用静态方法存储和获取线程内变量,不需要依赖于HttpSession.
 * web层的Controller可通过此处向business层传入user_id之类的变量
 *
 * @author spring.tu
 */
@SuppressWarnings("unchecked")
public class UserSession {
	private static final ThreadLocal sessionMap = new ThreadLocal();

	public static Object get(String attribute) {
		Map map = (Map) sessionMap.get();
		return map.get(attribute);
	}

	public <T> T get(String attribute, Class<T> clazz) {
		return (T) get(attribute);
	}

	public static void set(String attribute, Object value) {
		Map map = (Map) sessionMap.get();
		map.put(attribute, value);
	}
}

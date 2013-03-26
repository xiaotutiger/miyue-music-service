package com.miyue.exception;

/**
 * 业务异常基类.
 *
 * @author spring.tu
 */
@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}

package com.api.service.exception;

/**
 * 用户/token获取失效异常
 */
public class UKInvalidException extends RuntimeException {
    public UKInvalidException() {
        super();
    }

    public UKInvalidException(String message) {
        super(message);
    }

    public UKInvalidException(Throwable cause) {
        super(cause);
    }
}

package com.api.service.exception;

/**
 * 支付回调异常
 */
public class PayNotifyFindException extends RuntimeException {
    public PayNotifyFindException() {
        super();
    }

    public PayNotifyFindException(String message) {
        super(message);
    }

    public PayNotifyFindException(Throwable cause) {
        super(cause);
    }
}

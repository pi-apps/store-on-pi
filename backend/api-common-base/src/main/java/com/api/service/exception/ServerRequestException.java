package com.api.service.exception;

/**
 * 服务器请求调用异常
 */
public class ServerRequestException extends RuntimeException {
    public ServerRequestException() {
        super();
    }

    public ServerRequestException(String message) {
        super( message);
    }

    public ServerRequestException(Throwable cause) {
        super(cause);
    }
}

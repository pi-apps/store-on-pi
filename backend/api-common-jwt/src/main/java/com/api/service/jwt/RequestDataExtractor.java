package com.api.service.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.function.Consumer;

public class RequestDataExtractor {

    /**
     * 获取请求头
     */
    public static String getHeader(ServerHttpRequest request, String key) {
        List<String> list = request.getHeaders().get(key);
        if (list == null) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获得访问路径
     */
    public static String getPath(ServerHttpRequest request) {
        return request.getURI().getPath();
    }

    /**
     * 添加请求头
     */
    public static void addHeader(ServerWebExchange exchange, Consumer<HttpHeaders> httpHeaders) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest serverHttpRequest = request.mutate().headers(httpHeaders).build();
        exchange.mutate().request(serverHttpRequest).build();
    }
}

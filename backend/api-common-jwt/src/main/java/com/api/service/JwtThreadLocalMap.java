package com.api.service;


import com.api.service.db.entity.TApiUserEntity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ashy.cheung
 */
public class JwtThreadLocalMap {
    private static final ThreadLocal<ConcurrentHashMap<String, Object>> jwtThreadLocalMap = new ThreadLocal() {
        protected ConcurrentHashMap<String, Object> initialValue() {
            return new ConcurrentHashMap(4);
        }
    };

    public static ConcurrentHashMap<String, Object> queryjwtThreadLocalMap() {
        return jwtThreadLocalMap.get();
    }

    public static void deletejwtThreadLocalMap(String key) {
        jwtThreadLocalMap.get().remove(key);
    }

    public static void destroy() {
        jwtThreadLocalMap.remove();
    }

    public static TApiUserEntity getUser() {
        if (null == jwtThreadLocalMap.get().get("TApiUserEntity")) {
            return null;
        }
        return (TApiUserEntity) jwtThreadLocalMap.get().get("TApiUserEntity");
    }

    public static void setUser(TApiUserEntity tUserEntity) {
        ConcurrentHashMap<String, Object> map = jwtThreadLocalMap.get();
        if (null == tUserEntity) {
            map.remove("TApiUserEntity");
        } else {
            map.put("TApiUserEntity", tUserEntity);
        }
        jwtThreadLocalMap.set(map);
    }


}
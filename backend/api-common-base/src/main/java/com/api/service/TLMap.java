package com.api.service;


import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ashy.cheung
 */
public class TLMap {
    private static final ThreadLocal<ConcurrentHashMap<String, Object>> tlMap = new ThreadLocal() {
        protected ConcurrentHashMap<String, Object> initialValue() {
            return new ConcurrentHashMap(4);
        }
    };

    public static ConcurrentHashMap<String, Object> queryTLMap() {
        return tlMap.get();
    }

    public static Object getTlMap(String key) {
        return tlMap.get().get(key);
    }

    public static void deleteTlMap(String key) {
        tlMap.get().remove(key);
    }

    public static void setTlMap(String key, Object val) {
        ConcurrentHashMap<String, Object> map = tlMap.get();
        if (null == val) {
            map.put(key, "");
        } else {
            map.put(key, val);
        }
        tlMap.set(map);
    }

    public static String getUserId() {
        if (StringUtils.isBlank(tlMap.get().get("user_id") + "")) {
            return null;
        }
        return (String) tlMap.get().get("user_id");
    }

    public static void setUserId(String userId) {
        ConcurrentHashMap<String, Object> map = tlMap.get();
        if (StringUtils.isBlank(userId)) {
            map.remove("user_id");
        } else {
            map.put("user_id", userId);
        }
        tlMap.set(map);
    }

    public static String getMobile() {
        if (StringUtils.isBlank(tlMap.get().get("mobile") + "")) {
            return null;
        }
        return (String) tlMap.get().get("mobile");
    }

    public static void setMobile(String mobile) {
        ConcurrentHashMap<String, Object> map = tlMap.get();
        if (StringUtils.isBlank(mobile)) {
            map.remove("mobile");
        } else {
            map.put("mobile", mobile);
        }
        tlMap.set(map);
    }

    public static String getToken() {
        if (StringUtils.isBlank(tlMap.get().get("token") + "")) {
            return null;
        }
        return (String) tlMap.get().get("token");
    }

    public static void setToken(String token) {
        ConcurrentHashMap<String, Object> map = tlMap.get();
        if (StringUtils.isBlank(token)) {
            map.remove("token");
        } else {
            map.put("token", token);
        }
        tlMap.set(map);
    }

    public static boolean getAuthUser() {
        if (null == tlMap.get().get("isAuthUser")) {
            return false;
        }
        return (boolean) tlMap.get().get("isAuthUser");
    }

    public static void setAuthUser(boolean isAuthUser) {
        ConcurrentHashMap<String, Object> map = tlMap.get();
        map.put("isAuthUser", isAuthUser);
        tlMap.set(map);
    }

    public static void destroy() {

        tlMap.remove();
    }
}
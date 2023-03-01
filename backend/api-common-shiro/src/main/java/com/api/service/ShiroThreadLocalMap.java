package com.api.service;


import com.api.service.shiroEntity.ShiroTokenSessionEntity;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ashy.cheung
 */
public class ShiroThreadLocalMap {
    private static final ThreadLocal<ConcurrentHashMap<String, Object>> shiroThreadLocalMap = new ThreadLocal() {
        protected ConcurrentHashMap<String, Object> initialValue() {
            return new ConcurrentHashMap(4);
        }
    };

    public static ConcurrentHashMap<String, Object> queryshiroThreadLocalMap() {
        return shiroThreadLocalMap.get();
    }

    public static void deleteshiroThreadLocalMap(String key) {
        shiroThreadLocalMap.get().remove(key);
    }

    public static void destroy() {
        if (null != shiroThreadLocalMap) {
            shiroThreadLocalMap.remove();
        }

    }

    public static String getUserId() {
        if (StringUtils.isBlank((String) shiroThreadLocalMap.get().get("user_id"))) {
            return null;
        }
        return (String) shiroThreadLocalMap.get().get("user_id");
    }

    public static void setUserId(String userId) {
        ConcurrentHashMap<String, Object> map = shiroThreadLocalMap.get();
        if (StringUtils.isBlank(userId)) {
            map.remove("user_id");
        } else {
            map.put("user_id", userId);
        }
        shiroThreadLocalMap.set(map);
    }

    public static String getMobile() {
        if (StringUtils.isBlank((String) shiroThreadLocalMap.get().get("mobile"))) {
            return null;
        }
        return (String) shiroThreadLocalMap.get().get("mobile");
    }

    public static void setMobile(String mobile) {
        ConcurrentHashMap<String, Object> map = shiroThreadLocalMap.get();
        if (StringUtils.isBlank(mobile)) {
            map.remove("mobile");
        } else {
            map.put("mobile", mobile);
        }
        shiroThreadLocalMap.set(map);
    }

    public static String getToken() {
        if (StringUtils.isBlank((String) shiroThreadLocalMap.get().get("token"))) {
            return null;
        }
        return (String) shiroThreadLocalMap.get().get("token");
    }

    public static void setToken(String token) {
        ConcurrentHashMap<String, Object> map = shiroThreadLocalMap.get();
        if (StringUtils.isBlank(token)) {
            map.remove("token");
        } else {
            map.put("token", token);
        }
        shiroThreadLocalMap.set(map);
    }

    public static ShiroTokenSessionEntity getShiroTokenSessionEntity() {
        if (null == shiroThreadLocalMap.get().get("shiroTokenSessionEntity")) {
            return null;
        }
        return (ShiroTokenSessionEntity) shiroThreadLocalMap.get().get("shiroTokenSessionEntity");
    }

    public static void setShiroTokenSessionEntity(ShiroTokenSessionEntity shiroTokenSessionEntity) {
        ConcurrentHashMap<String, Object> map = shiroThreadLocalMap.get();
        if (null == shiroTokenSessionEntity) {
            map.remove("shiroTokenSessionEntity");
        } else {
            map.put("shiroTokenSessionEntity", shiroTokenSessionEntity);
        }
        shiroThreadLocalMap.set(map);
    }


}
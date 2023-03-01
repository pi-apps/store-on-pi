package com.api.service.shiroDao;


import com.api.service.ShiroThreadLocalMap;
import com.api.service.shiroEntity.ShiroTokenSessionEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;


/**
 * @author Ashy.Cheung
 * @Date 2020.07.04
 */
public class RedisSessionDao extends CachingSessionDAO {

    public static final String sessionToken = "syssessionToken:";

    private RedisTemplate redisTemplate;


    public RedisSessionDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    protected Serializable doCreate(Session session) {
        String sessionkey = sessionToken + ShiroThreadLocalMap.getUserId();
        assignSessionId(session, sessionkey);
        // 2020.11.02 修改共享缓存机制
        if (StringUtils.isNotBlank(ShiroThreadLocalMap.getToken())) {
            ShiroTokenSessionEntity shiroTokenSessionEntity = new ShiroTokenSessionEntity();
            shiroTokenSessionEntity.setSession(session);
            shiroTokenSessionEntity.setToken(ShiroThreadLocalMap.getToken());
            ShiroThreadLocalMap.setShiroTokenSessionEntity(shiroTokenSessionEntity);
        }
        return sessionkey;
    }


    @Override
    protected Session doReadSession(Serializable sessionId) {
        String sessionkey = sessionId.toString();
        // 2020.11.02 修改共享缓存机制
        ShiroTokenSessionEntity shiroTokenSessionEntity = ShiroThreadLocalMap.getShiroTokenSessionEntity();
        Session session = null;
        if (null != shiroTokenSessionEntity) {
            session = shiroTokenSessionEntity.getSession();
        }
        if (null == session) {
            shiroTokenSessionEntity = (ShiroTokenSessionEntity) redisTemplate.opsForValue().get(sessionkey);
            if (null != shiroTokenSessionEntity) {
                String token = shiroTokenSessionEntity.getToken();
                if (token.equals(ShiroThreadLocalMap.getToken())) {
                    session = shiroTokenSessionEntity.getSession();
                    ShiroThreadLocalMap.setShiroTokenSessionEntity(shiroTokenSessionEntity);
                }
            }
        }
        return session;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session s = doReadSession(sessionId);
        if (s == null) {
            throw new UnknownSessionException("There is no session with id [ " + sessionId + "]");
        }
        return s;
    }

    @Override
    protected void doUpdate(Session session) {
        ShiroTokenSessionEntity shiroTokenSessionEntity = new ShiroTokenSessionEntity();
        shiroTokenSessionEntity.setSession(session);
        shiroTokenSessionEntity.setToken(ShiroThreadLocalMap.getToken());
        ShiroThreadLocalMap.setShiroTokenSessionEntity(shiroTokenSessionEntity);
    }

    @Override
    protected void doDelete(Session session) {
        ShiroThreadLocalMap.setShiroTokenSessionEntity(null);
    }
}
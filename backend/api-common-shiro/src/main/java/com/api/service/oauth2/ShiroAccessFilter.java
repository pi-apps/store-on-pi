package com.api.service.oauth2;


import com.api.service.AuthInitAccessUtil;
import com.api.service.RedisUtil;
import com.api.service.ShiroThreadLocalMap;
import com.api.service.config.ShiroConfig;
import com.api.service.shiroDao.RedisSessionDao;
import com.api.service.shiroEntity.ShiroTokenSessionEntity;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author Ashy.Cheung
 * @Data 2020.07.04
 */

public class ShiroAccessFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            logger.info("Shiro监控跟踪-启动 : 当前请求接口{}", " url: " + req.getRequestURI());
            //调试模式校验通过
            if (AuthInitAccessUtil.swaggerDebug(req)) {
                logger.info("Shiro监控跟踪-结束 : 当前请求接口{}", " url: " + req.getRequestURI());
                chain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            logger.info("Shiro创建当前线程请求{}", " url: " + req.getRequestURI());
            //不鉴权请求通过
            if (unAuthCheck(req)) {
                chain.doFilter(request, response);
                return;
            }

            String user_id = req.getHeader("user_id");//从请求头取
            if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
                user_id = req.getParameter("user_id");//从参数获取一次
                if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
                    user_id = null;
                }
            }
            if (StringUtils.isNotBlank(user_id)) {
                ShiroThreadLocalMap.setUserId(user_id);
                logger.info("用户ID :     " + user_id + "      url: " + req.getRequestURI());
            }

            String token = req.getHeader("token");
            if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                token = req.getParameter("token");//从参数获取一次
                if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                    token = null;
                }
            }
            if (StringUtils.isNotBlank(token)) {
                ShiroThreadLocalMap.setToken(token);
                logger.info("用户token :     " + token + "      url: " + req.getRequestURI());
            }
            chain.doFilter(request, response);//继续下一个过滤器
            return;
        } catch (Exception e) {
            e.printStackTrace();
            AuthInitAccessUtil.responseException(response, e);
        } finally {
            try {
                String user_id = ShiroThreadLocalMap.getUserId();
                if (StringUtils.isNotBlank(user_id) && !user_id.toUpperCase().equals("NULL")) {
                    ShiroTokenSessionEntity shiroTokenSessionEntity = ShiroThreadLocalMap.getShiroTokenSessionEntity();
                    if (null != shiroTokenSessionEntity) {
                        String sessionId = RedisSessionDao.sessionToken + user_id;
                        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                        RedisUtil redisTemplate = (RedisUtil) factory.getBean("redisUtil");
                        redisTemplate.redisTemplate().opsForValue().set(sessionId, shiroTokenSessionEntity);
                        redisTemplate.redisTemplate().expire(sessionId, ShiroConfig.expireTime, TimeUnit.SECONDS);
                    }
                }
                ShiroThreadLocalMap.destroy();//销毁对象
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("Shiro销毁当前线程请求{}", " url: " + req.getRequestURI());
            logger.info("Shiro监控跟踪-结束 : 当前请求接口{}", " url: " + req.getRequestURI());
        }
    }

    /**
     * 不鉴权请求通过
     */
    private static boolean unAuthCheck(HttpServletRequest req) {
        if (
                AuthInitAccessUtil.urlSysCheck(req, "/unAuth/")
        ) {  //此处必须等于0,等于0为全匹配校验
            String user_id = req.getHeader("user_id");//从请求头取
            if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
                user_id = req.getParameter("user_id");//从参数获取一次
                if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
                    user_id = null;
                }
            }
            if (StringUtils.isNotBlank(user_id)) {
                ShiroThreadLocalMap.setUserId(user_id);
            }
            String token = req.getHeader("token");
            if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                token = req.getParameter("token");//从参数获取一次
                if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                    token = null;
                }
            }
            if (StringUtils.isNotBlank(token)) {
                ShiroThreadLocalMap.setToken(token);
            }

            return true;
        } else {
            return false;
        }
    }
}

package com.api.service.oauth2;

import com.api.service.RedisUtil;
import com.api.service.ShiroThreadLocalMap;
import com.api.service.http.HttpContextUtils;
import com.api.service.shiroDao.RedisSessionDao;
import com.api.service.shiroEntity.ShiroTokenSessionEntity;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * oauth2过滤器
 */
public class OAuth2Filter extends AuthenticatingFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String token = ShiroThreadLocalMap.getToken();
        if (StringUtils.isBlank(token)) {
            logger.error("token is null");
            return null;
        }
        return new UsernamePasswordToken(token, token);
    }

    //校验通过
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        ShiroTokenSessionEntity shiroTokenSessionEntity = ShiroThreadLocalMap.getShiroTokenSessionEntity();
        if (null != shiroTokenSessionEntity) {
            return true;
        } else {
            String userId = ShiroThreadLocalMap.getUserId();
            String token = ShiroThreadLocalMap.getToken();
            if (StringUtils.isBlank(userId) || StringUtils.isBlank(token)) {
                return false;
            }
            String sessionkey = RedisSessionDao.sessionToken + userId;
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            RedisUtil redisUtil = (RedisUtil) factory.getBean("redisUtil");
            shiroTokenSessionEntity = (ShiroTokenSessionEntity) redisUtil.redisTemplate().opsForValue().get(sessionkey);
            if (null != shiroTokenSessionEntity) {
                if (token.equals(shiroTokenSessionEntity.getToken())) {
                    ShiroThreadLocalMap.setShiroTokenSessionEntity(shiroTokenSessionEntity);
                    return true;
                }
            }
        }
        return false;


    }

    //拒绝访问
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", HttpStatus.SC_UNAUTHORIZED);
        map.put("msg", "invalid token");
        String json = new Gson().toJson(map);
        httpResponse.getWriter().print(json);
        return false;
    }

    //登录失败
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            //处理登录失败的异常
            Throwable throwable = e.getCause() == null ? e : e.getCause();
            Map<String, Object> map = new HashMap<>(2);
            map.put("code", HttpStatus.SC_UNAUTHORIZED);
            map.put("msg", throwable.getMessage());
            String json = new Gson().toJson(map);
            httpResponse.getWriter().print(json);
        } catch (IOException e1) {

        }
        return false;
    }
}

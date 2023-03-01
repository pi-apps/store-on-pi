package com.api.service.jwt;


import com.api.service.AuthInitAccessUtil;
import com.api.service.JwtThreadLocalMap;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.service.ITApiUserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Ashy.Cheung
 * @Data 2020.07.04
 */

public class JwtAccessFilter implements Filter {
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
            logger.info("JWT监控跟踪-启动 : 当前请求接口{}", " url: " + req.getRequestURI());

            if ("true".equals(req.getHeader("fegin"))) {
                chain.doFilter(request, response);
                logger.info("JWT监控跟踪-结束 : 当前请求接口{}", " url: " + req.getRequestURI());
                return;
            }
            //可直接请求通过
            if (AuthInitAccessUtil.adopt(req)) {
                chain.doFilter(request, response);
                logger.info("JWT监控跟踪-结束 : 当前请求接口{}", " url: " + req.getRequestURI());
                return;
            }

            //调试模式校验
            if (AuthInitAccessUtil.swaggerDebug(req)) {
                chain.doFilter(request, response);
                logger.info("JWT监控跟踪-结束 : 当前请求接口{}", " url: " + req.getRequestURI());
                return;
            }
            String token = req.getHeader("token");//从请求头取
            if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                token = req.getParameter("token");//从参数获取一次
                if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                    token = null;
                }
            }
            //不鉴权请求通过
            if (unAuthCheck(req)) {
                logger.info("JWT监控跟踪-结束 : 当前请求接口{}", " url: " + req.getRequestURI());
                //检查令牌是否存在,补充用户数据
                if (StringUtils.isNotBlank(token)) {
                    try {
                        //校验令牌合法性
                        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                        ITApiUserService itApiUserService = (ITApiUserService) factory.getBean("ITApiUserService");

                        Wrapper<TApiUserEntity> wrapper = new EntityWrapper<TApiUserEntity>()
                                .where("1=1")
                                .eq("access_token", token);
                        TApiUserEntity selectEnttiy = itApiUserService.selectOne(wrapper);
                        if (null == selectEnttiy) {
                            throw new RuntimeException("TOKEN验证不通过");
                        }
                       /*if (!token.equals(selectEnttiy.getToken())) {
                           throw new RuntimeException("登录失效,请重新登录");
                       }*/
                        if (!"0".equals(selectEnttiy.getStatus())) {
                            throw new RuntimeException("账号已被禁用,请联系客服");
                        }
                        JwtThreadLocalMap.setUser(selectEnttiy);
                    } catch (Exception e) {

                    }
                }
                chain.doFilter(request, response);
                return;
            }

            //检查令牌是否存在
            if (StringUtils.isBlank(token)) {
                throw new RuntimeException("令牌失效,请重新登录");
            }
            try {
                //校验令牌合法性

                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                ITApiUserService itApiUserService = (ITApiUserService) factory.getBean("ITApiUserService");
                Wrapper<TApiUserEntity> wrapper = new EntityWrapper<TApiUserEntity>()
                        .where("1=1")
                        .eq("access_token", token);
                TApiUserEntity selectEnttiy = itApiUserService.selectOne(wrapper);
                if (null == selectEnttiy) {
                    throw new RuntimeException("TOKEN验证不通过");
                }
               /* if (!token.equals(selectEnttiy.getToken())) {
                    throw new RuntimeException("登录失效,请重新登录");
                }*/
                if (!"0".equals(selectEnttiy.getStatus())) {
                    throw new RuntimeException("账号已被禁用,请联系客服");
                }
                JwtThreadLocalMap.setUser(selectEnttiy);
            } catch (Exception e) {
                AuthInitAccessUtil.responseMSG(response, 401, e.getMessage());
                return;
            }

            logger.info("JWT监控跟踪-已启动本地线程 : 当前请求接口{}", " url: " + req.getRequestURI());
            chain.doFilter(request, response);//继续下一个过滤器
            logger.info("JWT监控跟踪-正常关闭本地线程 : 当前请求接口{}", " url: " + req.getRequestURI());
        } catch (Exception e) {
            logger.info("JWT监控跟踪-异常 : 当前请求接口{}", " url: " + req.getRequestURI());
            e.printStackTrace();
            AuthInitAccessUtil.responseException(response, e);
        } finally {
            try {
                JwtThreadLocalMap.destroy();
            } catch (Exception e) {

            }
            logger.info("JWT监控跟踪-结束 : 当前请求接口{}", " url: " + req.getRequestURI());

        }
    }

    /**
     * 不鉴权请求通过
     */
    private static boolean unAuthCheck(HttpServletRequest req) {
        Environment env = (Environment) WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext()).getBean("environment");
        String applicationName = env.getProperty("spring.application.name", String.class);

        switch (applicationName) {
            case "cert-service": //认证服务不鉴权
                return true;
            case "cms-service": //内管服务不鉴权
                return true;
            default:
                if (req.getRequestURI().contains("/unAuth/")) {//遇到关键字unAuth不鉴权
                    return true;
                } else {
                    return false;
                }
        }


    }
}

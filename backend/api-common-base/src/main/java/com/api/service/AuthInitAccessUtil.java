package com.api.service;


import com.api.service.exception.UKInvalidException;
import com.api.service.http.HttpContextUtils;
import com.api.service.http.HttpStatusUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AuthInitAccessUtil {


    private final static String[] swaggerUrl = new String[]{
            "/swagger-resources",
            "/v2/api-docs",
            "/v2/api-docs-ext",
            "/doc.html",
            "/webjars"
    };


    /**
     * swagger调试
     */
    public static boolean swaggerDebug(HttpServletRequest req) {
        Environment env = (Environment) WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext()).getBean("environment");
        boolean swaggerEnabled = env.getProperty("swagger.enabled", boolean.class);
        if (swaggerEnabled) {//调试已打开
            for (String s : swaggerUrl) {
                if (0 == req.getRequestURI().indexOf(s)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }


    /**
     * 检查请求是否带有用户Id及token
     */
    public static void authUserAndToken(HttpServletRequest req) {
        String user_id = req.getHeader("user_id");//从请求头取
        if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
            user_id = req.getParameter("user_id");//从参数获取一次
            if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
                throw new UKInvalidException("失效[U],服务端终止请求");
            }
        }
        TLMap.setUserId(user_id);
        String token = req.getHeader("token");
        if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
            token = req.getParameter("token");//从参数获取一次
            if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                throw new UKInvalidException("失效[T],服务端终止请求");
            }
        }
        TLMap.setToken(token);


    }

    /**
     * 方法针对于不鉴权接口,场景:
     * 有些不鉴权接口也会允许登录用户进行自身的ID匹配,
     * 为防止被他人冒用非自己的userId进行查询,所以将userId,token设为全局前,进行身份鉴权
     */
    public static void apendUserAndToken(HttpServletRequest req) {
        String user_id = req.getHeader("user_id");//从请求头取
        if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
            user_id = req.getParameter("user_id");//从参数获取一次
            if (StringUtils.isBlank(user_id) || user_id.toUpperCase().equals("NULL")) {
                user_id = null;
            }
        }
        String token = req.getHeader("token");
        if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
            token = req.getParameter("token");//从参数获取一次
            if (StringUtils.isBlank(token) || token.toUpperCase().equals("NULL")) {
                token = null;
            }
        }


        if (StringUtils.isNotBlank(user_id) && StringUtils.isNotBlank(token)) {
            try {
                //tokenRedisCheck(req, user_id, token);
                TLMap.setUserId(user_id);
                TLMap.setToken(token);
            } catch (Exception e) {
                /**
                 * 此处异常不需要处理,允许登录用户对不鉴权接口进行userId查询
                 */
            }
        }


    }
    public static boolean urlSysCheck(HttpServletRequest req, String url) {
        if (0 == req.getRequestURI().indexOf(url)) {  //此处必须等于0,等于0为全匹配校验
            return true;
        } else {
            return false;
        }
    }
    /**
     * 通用校验请求
     */
    public static boolean adopt(HttpServletRequest req) {
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;  //允许vue前端尝试请求
        } else if ("/favicon.ico".equals(req.getRequestURI())) {
            return true; //允许浏览器商标请求
        } else {
            return false;
        }
    }

    /**
     * 通用报错返回前端
     */
    public static void responseException(ServletResponse response, Exception e) {
        if (e.getMessage().contains("Subject does not have permission")) {
            responseMSG(response, HttpStatusUtil.SC_AUTH_UNKNOWN, "该功能需要管理员授权后才能使用!");
        } else if (e.getClass().getName().contains("UKInvalidException")) {
            responseMSG(response, HttpStatusUtil.SC_SIGN_IN, e.getMessage());
        } else if (e.getClass().getName().contains("DataSourceNoFindException")) {
            responseMSG(response, HttpStatusUtil.SC_DATA_SOURCE, e.getMessage());
        } else if (e.getClass().getName().contains("ServerRequestException")) {
            responseMSG(response, HttpStatusUtil.SC_SERVER_REQUEST, e.getMessage());
        } else if (e.getClass().getName().contains("PayNotifyFindException")) {
            responseMSG(response, HttpStatusUtil.SC_PAY_NOTIFYFIND, e.getMessage());
        } else if (e.getMessage().contains("org.apache.shiro.authc.UsernamePasswordToken")) {
            responseMSG(response, HttpStatusUtil.SC_AUTH_LOGIN, "登录异常,请联系管理员");
        } else {
            responseMSG(response, HttpStatusUtil.SC_UNKNOWN, e.getMessage());
        }
    }

    public static void responseMSG(ServletResponse response, int code, String msg) {
        HttpServletResponse rq = (HttpServletResponse) response;
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", code);
        map.put("msg", msg);
        JSONObject responseJSONObject = JSONObject.fromObject(map);
//        rq.setHeader("access-Control-Allow-Credentials", "true");
//        rq.setHeader("access-Control-Allow-Origin", HttpContextUtils.getOrigin());
//        rq.setHeader("access-Control-Allow-Origin", "*");
//        // 设置所有类型的头信息都可以接收.允许自定义头信息
//        rq.setHeader("access-Control-Allow-Headers", "*");
        rq.setCharacterEncoding("UTF-8");
        rq.setContentType("application/json; charset=utf-8");
        rq.setStatus(200);
        PrintWriter out = null;
        try {
            out = rq.getWriter();
            out.append(responseJSONObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}

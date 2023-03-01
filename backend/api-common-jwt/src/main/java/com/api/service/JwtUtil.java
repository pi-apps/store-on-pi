package com.api.service;

import com.alibaba.fastjson.JSON;
import com.api.service.jwt.JwtSign;
import com.api.service.jwtEntity.JwtUserEntity;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * 创建token时,必须传入当前user对象的坐标地区,手机型号,并且修改数据库常用地址,常用手机型号,保证令牌不被盗走
 */
public class JwtUtil {
    private static final String keySecret = "qangtD5s6Wm24GLFotc";

    /**
     * 生成用户token
     */
    public static String createToken(JwtUserEntity user, Long date) {
        //根据用户信息和密钥得到token
        String jwtToken = JwtSign.generateSessionToken(user, keySecret, date); //30 * 24 * 3600 * 1000L = 30日
        System.out.println(jwtToken);
        return jwtToken;

    }

    public static JwtUserEntity verifyToken(String token) {
        //根据用户信息和密钥得到token
        //校验token
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JwtSign.verifySessionToken(token, keySecret);
        } catch (TokenExpiredException e) {
            throw new RuntimeException("登录状态已失效，请重新登录");
        } catch (Exception e) {
            throw new RuntimeException("您还未登录哟");
        }
        String json = decodedJWT.getClaims().get(JwtSign.JWT_USER).asString();

        JwtUserEntity jwtUserEntity = JSON.parseObject(json, JwtUserEntity.class);

        if (jwtUserEntity.getExpireTime().getTime() < System.currentTimeMillis()) {

            throw new RuntimeException("登录已失效[T]，请重新登录");
        }
        return jwtUserEntity;
    }

    private static boolean checkParams(String param, String... params) {
        for (String temp : params) {
            if (param.equals(temp)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {


        JwtUserEntity jwtUserEntity = new JwtUserEntity();

        jwtUserEntity.setUserId("c45aafa7-10e0-49f0-974b-40af8d457561");

        Long date = 30 * 24 * 3600 * 1000L; //30日
        jwtUserEntity.setExpireTime(new Date(new Date().getTime() + date));

        //校验通过后,生成token
        String token = JwtUtil.createToken(jwtUserEntity, date);


        System.out.println(verifyToken(token));


    }

}

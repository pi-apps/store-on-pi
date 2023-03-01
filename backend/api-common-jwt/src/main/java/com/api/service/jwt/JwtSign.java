package com.api.service.jwt;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtSign {
    //token中包含的参数，可自行添加
    public static final String JWT_USER = "user";
    private static Map<String, JWTVerifier> verifierMap = new HashMap<>();
    private static Map<String, Algorithm> algorithmMap = new HashMap<>();

    /**
     * 生成Token
     *
     * @param user         用户信息
     * @param signingToken 密钥
     * @param duration     过期时间
     * @return token
     */
    public static String generateSessionToken(Object user, String signingToken, long duration) {
        if (StringUtils.isEmpty(signingToken)) {
            throw new RuntimeException("No signing token present");
        }
        Algorithm algorithm = getAlgorithm(signingToken);
        String token = JWT.create()
                .withClaim(JWT_USER, JSON.toJSONString(user))
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .sign(algorithm);
        return token;
    }

    /**
     * 校验token
     *
     * @param tokenString  token
     * @param signingToken 密钥
     * @return
     */
    public static DecodedJWT verifySessionToken(String tokenString, String signingToken) {
        return verifyToken(tokenString, signingToken);
    }

    static DecodedJWT verifyToken(String tokenString, String signingToken) {
        JWTVerifier verifier = verifierMap.get(signingToken);
        if (verifier == null) {
            synchronized (verifierMap) {
                verifier = verifierMap.get(signingToken);
                if (verifier == null) {
                    Algorithm algorithm = Algorithm.HMAC512(signingToken);
                    verifier = JWT.require(algorithm).build();
                    verifierMap.put(signingToken, verifier);
                }
            }
        }

        DecodedJWT jwt = verifier.verify(tokenString);
        return jwt;
    }

    private static Algorithm getAlgorithm(String signingToken) {
        Algorithm algorithm = algorithmMap.get(signingToken);
        if (algorithm == null) {
            synchronized (algorithmMap) {
                algorithm = algorithmMap.get(signingToken);
                if (algorithm == null) {
                    algorithm = Algorithm.HMAC512(signingToken);
                    algorithmMap.put(signingToken, algorithm);
                }
            }
        }
        return algorithm;
    }

}
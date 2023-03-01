package com.api.service.jwt;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtHash {
    /**
     * HmacSHA256加密
     *
     * @param key  密钥
     * @param data 数据
     * @return 加密结果
     */
    public static String encode(String key, String data) {
        String code = null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            code = Hex.toHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException("密码编码错误");
        }
        return code;
    }
}
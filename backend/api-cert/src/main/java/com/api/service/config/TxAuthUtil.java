package com.api.service.config;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 腾讯云应用市场地址:
 * https://market.cloud.tencent.com/products/25464#params=eyJzcGVjIjoiMS4wMOWFgy8yMOasoSIsImN5Y2xlIjoiMeW5tCIsImJpbGxpbmdMaXN0IjpbXX0=
 * 移动端sdk地址
 * https://doc.geekforbest.com/web/#/2?page_id=62
 */

public class TxAuthUtil {

    private static String SecretID = "AKIDDqzvAKuGLhqu4JKqc48A97vecT78VLyp9A0U",
            SecretKey = " H60l7shSjFIi5dqI92D25BI67Py1QxbBI77M8r02";



    public static void main(String[] args) throws Exception {
        authorization("你的token");
    }

    /**
     * 核心调用方法,校验当前手机号码是否本机号码
     * @param token
     * @return
     * @throws Exception
     */
    public static String authorization(String token) throws Exception {
        //云市场分配的密钥Id
        String secretId = SecretID;
        //云市场分配的密钥Key
        String secretKey = SecretKey;
        String source = "market";

        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String datetime = sdf.format(cd.getTime());
        // 签名
        String auth = calcAuthorization(source, secretId, secretKey, datetime);
        // 请求方法
        String method = "POST";
        // 请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Source", source);
        headers.put("X-Date", datetime);
        headers.put("Authorization", auth);

        // 查询参数
        Map<String, String> queryParams = new HashMap<>();

        // body参数
        Map<String, String> bodyParams = new HashMap<>();
        bodyParams.put("appId", secretId);
        bodyParams.put("appKey", secretKey);
        bodyParams.put("token", token);
        // url参数拼接
        String url = "https://service-mxd4ekp3-1255701024.sh.apigw.tencentcs.com/release/superapi/super/flashsdk/mobile-query";
        if (!queryParams.isEmpty()) {
            url += "?" + urlencode(queryParams);
        }

        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod(method);

            // request headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // request body
            Map<String, Boolean> methods = new HashMap<>();
            methods.put("POST", true);
            methods.put("PUT", true);
            methods.put("PATCH", true);
            Boolean hasBody = methods.get(method);
            if (hasBody != null) {
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                conn.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(urlencode(bodyParams));
                out.flush();
                out.close();
            }

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }

            System.out.println(result);
            return result;
        } catch (Exception e) {

            System.out.println(e);
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    private static String calcAuthorization(String source, String secretId, String secretKey, String datetime)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String signStr = "x-date: " + datetime + "\n" + "x-source: " + source;
        Mac mac = Mac.getInstance("HmacSHA1");
        Key sKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), mac.getAlgorithm());
        mac.init(sKey);
        byte[] hash = mac.doFinal(signStr.getBytes("UTF-8"));
        String sig = new BASE64Encoder().encode(hash);

        String auth = "hmac id=\"" + secretId + "\", algorithm=\"hmac-sha1\", headers=\"x-date x-source\", signature=\"" + sig + "\"";
        return auth;
    }

    private static String urlencode(Map<?, ?> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    URLEncoder.encode(entry.getKey().toString(), "UTF-8"),
                    URLEncoder.encode(entry.getValue().toString(), "UTF-8")
            ));
        }
        return sb.toString();
    }
}
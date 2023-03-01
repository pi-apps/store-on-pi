package com.api.service.sdk.baiduMap;

import com.alibaba.fastjson.JSONObject;
import com.api.service.http.HttpClientUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

//java版计算signature签名
public class BaiduMapSdkUtil {


    private static String ak = "";
    private static String sn = "";
    private static String baseUrl = "";

    public static void main(String[] args) throws Exception {
        JSONObject jsonObject = getReverseGeocoding("104.1454", "30.818222");

        System.out.println(jsonObject.getString("province"));
        System.out.println(jsonObject.getString("city"));
        System.out.println(jsonObject.getString("district"));
        System.out.println(jsonObject.getString("street"));

    }

    /**
     * @param lng 经度
     * @param lat 纬度
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static JSONObject getReverseGeocoding(String lng, String lat) throws Exception {
        //地址
        String reverseGeocodingUrl = "/reverse_geocoding/v3?";
        //参数
        Map paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("ak", ak);
        paramsMap.put("output", "json");
        paramsMap.put("coordtype", "bd09ll");
        paramsMap.put("location", lat + "," + lng);

        String paramsStr = toQueryString(paramsMap);

        String wholeStr = new String(reverseGeocodingUrl + paramsStr + sn);
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
        String url = baseUrl + reverseGeocodingUrl + paramsStr + "&sn=" + MD5(tempStr);
        String resp = HttpClientUtil.sendGet(url);
        JSONObject jsonObject = JSONObject.parseObject(resp);

        if (null != jsonObject && 0 == jsonObject.getInteger("status")) {
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject addressComponent = result.getJSONObject("addressComponent");
            return addressComponent;
        }
        throw new RuntimeException("解析失败");
    }

    private static String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            queryString.append(URLEncoder.encode((String) pair.getValue(),
                    "UTF-8") + "&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    private static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}


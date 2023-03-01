package com.api.service.sdk.baiduCloud;


import com.alibaba.fastjson.JSONArray;
import com.api.service.sdk.baiduCloud.sdk.ocr.AipOcr;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;

public class BaiduCloudOCRUtil {
    //设置APPID/AK/SK
    private static final String APP_ID = BaiduCloudConfig.app_id;
    private static final String API_KEY = BaiduCloudConfig.api_key;
    private static final String SECRET_KEY = BaiduCloudConfig.secret_key;


    /**
     * 图片识别
     *
     * @param url  腾讯云服务器地址
     * @param type [1身份证资料面][2身份证国徽面]
     * @return
     */
    public static com.alibaba.fastjson.JSONObject OCR(String url, String type) {
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 调用接口
        JSONObject res = client.basicGeneralUrl(url, new HashMap<>());
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(res.toString());
        switch (type) {
            case "1": //身份证资料面
                return certFront(jsonObject);
            case "2": //身份证国徽面
                return certBack(jsonObject);
            default:
                throw new RuntimeException("类型错误");
        }
    }


    private static com.alibaba.fastjson.JSONObject certFront(com.alibaba.fastjson.JSONObject jsonObject) {
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        for (int i = 0; i < jsonArray.size(); i++) {
            Object o = jsonArray.get(i);
            com.alibaba.fastjson.JSONObject temp = com.alibaba.fastjson.JSONObject.parseObject(o.toString());
            String words = temp.getString("words");
            if (words.contains("姓名")) {

                words = words.substring(2);
                result.put("certName", words);

            } else if (words.contains("性别")) {

                words = words.substring(2, 3);
                result.put("sex", words);

            } else if (words.contains("出生")) {

                words = words.substring(2);
                result.put("birthday", words);

            } else if (words.contains("住址")) {

                words = words.substring(2);
                result.put("address", words);

            } else if (words.contains("公民身份号码")) {
                words = words.substring(6);
                if (StringUtils.isBlank(words)) {
                    o = jsonArray.get(i + 1);
                    temp = com.alibaba.fastjson.JSONObject.parseObject(o.toString());
                    words = temp.getString("words");
                    result.put("certNo", words);
                } else {
                    result.put("certNo", words);
                }

            }

        }
        return result;
    }

    private static com.alibaba.fastjson.JSONObject certBack(com.alibaba.fastjson.JSONObject jsonObject) {
        com.alibaba.fastjson.JSONObject result = new com.alibaba.fastjson.JSONObject();
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        for (Object o : jsonArray) {
            com.alibaba.fastjson.JSONObject temp = com.alibaba.fastjson.JSONObject.parseObject(o.toString());
            String words = temp.getString("words");
            if (words.contains("签发机关")) {
                words = words.substring(4);
                result.put("org", words);

            } else if (words.contains("有效期限")) {

                words = words.substring(4);
                String[] ss = words.split("-");
                try {
                    String start = ss[0].replace(".", "");
                    result.put("start", start);
                } catch (Exception e) {

                }
                try {
                    String end = ss[1].replace(".", "");
                    result.put("end", end);
                } catch (Exception e) {

                }


            }

        }
        return result;
    }
}

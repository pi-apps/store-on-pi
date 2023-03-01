package com.api.service.sdk.kdn;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class KdUtil {


    /**
     * @param logistics_no 物流单号
     * @param customer_name (顺丰必填手机号码)(SF开头单号就是顺丰),其他不填
     * @return
     * @throws Exception
     */
    public static JSONObject queryOrderByKdn(String logistics_no, String customer_name) throws Exception {
        String json = discern(logistics_no);
        JSONObject jsonObject = JSONObject.parseObject(json);
        String logisticCode = jsonObject.getString("LogisticCode");
        String shipperCode = null;
        JSONArray jsonArray = jsonObject.getJSONArray("Shippers");
        if (null != jsonArray && 1 == jsonArray.size()) {
            JSONObject jsonObject1 = JSONObject.parseObject(jsonArray.get(0).toString());
            shipperCode = jsonObject1.getString("ShipperCode");
            String resp;
            if ("SF".equals(shipperCode)) {
                customer_name = customer_name.substring(7);
                resp = query(logisticCode, shipperCode, customer_name);
            } else {
                resp = query(logisticCode, shipperCode, null);
            }

            JSONObject jsonObject2 = JSONObject.parseObject(resp);
            jsonObject2.put("ShipperName", jsonObject1.getString("ShipperName"));
            return jsonObject2;
        } else {
            //快递单号识别到多个合作渠道
        }
        return null;
    }

    //单号识别接口
    private static String discern(String logisticCode) throws Exception {
        String ReqURL = "https://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";
        // 组装应用级参数
        String RequestData = "{" +
                "'LogisticCode': '" + logisticCode + "'" +
                "}";
        // 组装系统级参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(RequestData, "UTF-8"));
        params.put("EBusinessID", KdnConfig.EBusinessID);
        params.put("RequestType", "2002");
        String dataSign = encrypt(RequestData, KdnConfig.ApiKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        // 以form表单形式提交post请求，post请求体中包含了应用级参数和系统级参数
        String result = sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......
        return result;
    }


    //快递查询接口
    private static String query(String logisticCode, String shipperCode, String customerName) throws Exception {
        String ReqURL = "https://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";
        // 组装应用级参数
        String RequestData = "{" +
                "'OrderCode': ''," +
                "'CustomerName': '" + customerName + "'," +
                "'ShipperCode': '" + shipperCode + "'," +
                "'LogisticCode': '" + logisticCode + "'," +
                "}";
        // 组装系统级参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(RequestData, "UTF-8"));
        params.put("EBusinessID", KdnConfig.EBusinessID);
        params.put("RequestType", "8001");//快递查询接口指令8002/地图版快递查询接口指令8004
        String dataSign = encrypt(RequestData, KdnConfig.ApiKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");
        // 以form表单形式提交post请求，post请求体中包含了应用级参数和系统级参数
        String result = sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......
        return result;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * MD5加密
     * str 内容
     * charset 编码方式
     *
     * @throws Exception
     */
    private static String MD5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer(32);
        for (int i = 0; i < result.length; i++) {
            int val = result[i] & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    /**
     * base64编码
     * str 内容
     * charset 编码方式
     *
     * @throws UnsupportedEncodingException
     */
    private static String base64(String str, String charset) throws UnsupportedEncodingException {
        String encoded = Base64.encode(str.getBytes(charset));
        return encoded;
    }

    private static String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(str, charset);
        return result;
    }

    /**
     * 电商Sign签名生成
     * content 内容
     * keyValue ApiKey
     * charset 编码方式
     *
     * @return DataSign签名
     * @throws UnsupportedEncodingException ,Exception
     */
    private static String encrypt(String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception {
        if (keyValue != null) {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * url 发送请求的 URL
     * params 请求的参数集合
     *
     * @return 远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (param.length() > 0) {
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
}

package com.api.service.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @author Ashy.Cheung
 * @http 请求工具类
 * @date 2017.11.10
 */
public class HttpClientUtil {

    public static String sendGet(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String result = null;
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     *
     * @param url
     * @param charsetName 返回字符集
     * @return
     */
    public static String sendGet(String url, String charsetName) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            // 将返回的输入流转换成字符串
            inputStream = urlConnection.getInputStream();
            // 指定编码格式
            if (StringUtils.isBlank(charsetName)) {
                charsetName = "UTF-8";
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
            BufferedReader in = new BufferedReader(inputStreamReader);
            String jsonUserStr = in.readLine();
            return jsonUserStr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                urlConnection.disconnect();
            } catch (Exception e) {

            }
            try {
                if (null != urlConnection) {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {

            }
        }

    }
    /**
     * 发送HttpPost请求，参数为String
     * 接收端以流形式接收
     */
    public static String sendPost(String url, String param) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        StringEntity strEntity = null;
        try {
            strEntity = new StringEntity(param, "UTF-8");
            strEntity.setContentType("application/json");
        } catch (Exception e1) {

            e1.printStackTrace();
        }
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(strEntity);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity1 = response.getEntity();
            result = EntityUtils.toString(entity1);

        } catch (IOException e) {
            //  e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {

            }
        }

        return result;
    }

    /**
     * 发送不带参数的HttpPost请求
     */
    public static String sendPost(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(entity);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {

            }
        }
        return result;
    }

}
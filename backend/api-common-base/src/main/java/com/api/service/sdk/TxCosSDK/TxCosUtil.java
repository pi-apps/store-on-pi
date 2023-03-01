package com.api.service.sdk.TxCosSDK;

import com.api.service.ImgUtil;
import com.api.service.annotations.TxCosUrl;
import com.api.service.sdk.TxCosSDK.cos.CosPublicEntity;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * 腾讯云COS工具类
 */
@Component("TxCosUtil")
public class TxCosUtil {

    /**
     * 检查文件是否重名
     *
     * @param objectKey
     * @return
     * @throws RuntimeException
     */
    public boolean checkFile(String objectKey) throws RuntimeException {
        Map map = getCommon(objectKey);
        COSClient cosClient = null;
        cosClient = (COSClient) map.get(0);
        CosPublicEntity model = (CosPublicEntity) map.get(1);
        if (cosClient.doesObjectExist(String.format("%s-%s", model.getBucketName(), model.getAppId()), model.getObjectKey())) {
            return false;
        }
        return true;
    }


    public void fileUpload(String objectKey, InputStream inputStream) throws RuntimeException {
        Map map = getCommon(objectKey);
        COSClient cosClient = null;
        try {
            cosClient = (COSClient) map.get(0);
            CosPublicEntity model = (CosPublicEntity) map.get(1);
            if (cosClient.doesObjectExist(String.format("%s-%s", model.getBucketName(), model.getAppId()), model.getObjectKey())) {
                throw new RuntimeException("此文件重名,请更改文件名重试!");
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
            PutObjectRequest putObjectRequest = new PutObjectRequest(model.getBucketName() + "-" + model.getAppId(), objectKey, inputStream, objectMetadata);
            putObjectRequest.setMetadata(objectMetadata);
            // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
            putObjectRequest.setStorageClass(StorageClass.Standard);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            String eTag = putObjectResult.getETag();
            if (StringUtils.isBlank(eTag)) {
                throw new RuntimeException("文件直传失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("文件直传失败:" + e.getMessage());
        } finally {
            cosClient.shutdown();
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * COS获取下载签名URL
     *
     * @param objectKey 文件对象key
     * @return 签名URL
     */
    public String getCosObjectDownAuthUrl(String objectKey) throws RuntimeException {
        Map map = getCommon(objectKey);
        COSClient cosClient = null;
        try {
            cosClient = (COSClient) map.get(0);
            CosPublicEntity model = (CosPublicEntity) map.get(1);
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(String.format("%s-%s", model.getBucketName(), model.getAppId()), model.getObjectKey(), HttpMethodName.GET);
            // 这里设置签名在半个小时后过期
            Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
            req.setExpiration(expirationDate);
            URL url = cosClient.generatePresignedUrl(req);
            String urlstr = url.toString();
            urlstr = urlstr.replace("http", "https");
            return urlstr;
        } catch (Exception e) {
            throw new RuntimeException("获取下载签名URL失败");
        } finally {
            cosClient.shutdown();
        }
    }

    /**
     * COS获取下载签名URL
     *
     * @param objectKey 文件对象key
     * @return 签名URL
     */
    public String getCosObjectDownAuthUrl(String objectKey, long time) throws RuntimeException {
        Map map = getCommon(objectKey);
        COSClient cosClient = null;
        try {
            cosClient = (COSClient) map.get(0);
            CosPublicEntity model = (CosPublicEntity) map.get(1);
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(String.format("%s-%s", model.getBucketName(), model.getAppId()), model.getObjectKey(), HttpMethodName.GET);
            // 自定义设置过期时间
            Date expirationDate = new Date(System.currentTimeMillis() + time);
            req.setExpiration(expirationDate);
            URL url = cosClient.generatePresignedUrl(req);
            String urlstr = url.toString();
            urlstr = urlstr.replace("http", "https");
            return urlstr;
        } catch (Exception e) {
            throw new RuntimeException("获取下载签名URL失败");
        } finally {
            cosClient.shutdown();
        }
    }

    /**
     * COS获取上传签名URL
     *
     * @param objectKey 文件对象key
     * @return 签名URL
     */
    public String getCosObjectUploadAuthUrl(String objectKey) throws RuntimeException {
        Map map = getCommon(objectKey);
        COSClient cosClient = null;
        try {
            cosClient = (COSClient) map.get(0);
            CosPublicEntity model = (CosPublicEntity) map.get(1);
            if (cosClient.doesObjectExist(String.format("%s-%s", model.getBucketName(), model.getAppId()), model.getObjectKey())) {
                throw new RuntimeException("此文件重名,请更改文件名重试!");
            }
            Date expirationTime = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
            URL url = cosClient.generatePresignedUrl(String.format("%s-%s", model.getBucketName(), model.getAppId()), model.getObjectKey(), expirationTime, HttpMethodName.PUT);
            String urlstr = url.toString();
            urlstr = urlstr.replace("http", "https");
            return urlstr;
        } catch (Exception e) {
            throw new RuntimeException("获取上传签名URL失败" + e.getMessage());
        } finally {
            cosClient.shutdown();
        }
    }


    /**
     * 删除存储对象
     *
     * @param objectKey 文件对象key
     * @return 签名URL
     */
    public void deleteObject(String objectKey) throws RuntimeException {
        Map map = getCommon(objectKey);
        COSClient cosClient = null;
        try {
            cosClient = (COSClient) map.get(0);
            CosPublicEntity model = (CosPublicEntity) map.get(1);
            // 指定对象所在的存储桶
            cosClient.deleteObject(String.format("%s-%s", model.getBucketName(), model.getAppId()), model.getObjectKey());
        } catch (RuntimeException clientException) {
            throw new RuntimeException("删除存储对象失败");
        } finally {
            cosClient.shutdown();
        }
    }

    /**
     * client公共参数
     *
     * @param objectKey 文件对象key
     * @return
     */
    private Map getCommon(String objectKey) throws RuntimeException {
        CosPublicEntity cosPublicEntity = CosPublicEntity.build(objectKey);
        COSCredentials cred = new BasicCOSCredentials(cosPublicEntity.getSecretId(), cosPublicEntity.getSecretKey());
        Region region = new Region(cosPublicEntity.getRegionName());
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        Map map = new HashMap();
        map.put(0, cosClient);
        map.put(1, cosPublicEntity);
        return map;
    }

    /**
     * client公共参数
     */
    private COSClient bucketCommon(CosPublicEntity cosPublicEntity) {
        COSCredentials cred = new BasicCOSCredentials(cosPublicEntity.getSecretId(), cosPublicEntity.getSecretKey());
        Region region = new Region(cosPublicEntity.getRegionName());
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    /**
     * 绝对路径更换为相对路径(不带压缩)
     *
     * @param url 绝对路径
     * @return 相对路径
     */
    public String getRelativePathOld(String url) {
        url = url.substring(url.indexOf(".com") + 4, url.indexOf("?"));
        return url;
    }

    /**
     * 绝对路径更换为相对路径(带压缩)
     *
     * @param url 绝对路径
     * @return 相对路径
     */
    public String getRelativePath(String url) {
        url = url.substring(url.indexOf(".com") + 4, url.indexOf("?"));

        //上传的如果是图片格式后缀，取webp格式存数据库
        String houzhu = "jpg,jpeg,png,bmp,gif,heif,avif,tpg";
        String suffix = url.substring(url.lastIndexOf(".") + 1);//后缀名
        if (houzhu.contains(suffix)) {
            url = url.replaceAll(suffix, "webp");
        }
        return url;
    }

    /**
     * 远程连接上传图片
     *
     * @param name
     * @param urlPath
     * @return
     */
    public void netUpload(String name, String urlPath) {
        boolean status = checkFile(name);
        if (!status) {
            //重名直接返回
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlPath)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                byte[] bytes = response.body().bytes();
                byte[] bytes1 = ImgUtil.compressPicForScale(bytes, 100);//图片压缩
                InputStream sbs = new ByteArrayInputStream(bytes1);
                this.fileUpload(name, sbs);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 实体类中的属性值转换为相对路径
     *
     * @param entity 实体类
     */
    public void convertPathToCos(Object entity) {
        try {


            Class<?> entityClass = entity.getClass();
            ArrayList<String> ignoreField = new ArrayList<>();
            for (Field field : entityClass.getDeclaredFields()) {
                if (ignoreField.contains(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(TxCosUrl.class)) {
                    TxCosUrl annotation = field.getAnnotation(TxCosUrl.class);
                    String anSplit = annotation.split();
                    String anFieldKey = annotation.fieldKey();
                    field.setAccessible(true);
                    Field fillFiled = null;
                    try {
                        //查询是否存在该字段
                        if (StringUtils.isNotBlank(anSplit) && StringUtils.isNotBlank(anFieldKey)) {
                            fillFiled = entityClass.getDeclaredField(anFieldKey);
                            fillFiled.setAccessible(true);
                        }

                    } catch (NoSuchFieldException ignored) {
                    }
                    if (String.class == field.getType()) {
                        String o = (String) field.get(entity);

                        if (StringUtils.isNotBlank(o)) {
                            if (null != fillFiled) {

                                String[] split = StringUtils.split(o, anSplit);
                                for (int i = 0; i < split.length; i++) {
                                    split[i] = getRelativePath(split[i]);
                                }
                                fillFiled.set(entity, new ArrayList<>(Arrays.asList(split)));
                                ignoreField.add(anFieldKey);
                            } else {
                                field.setAccessible(true);
                                field.set(entity, getRelativePath(o));
                            }


                        }
                    } else if ("java.util.List<java.lang.String>".equals(String.valueOf(field.getGenericType()))) {
                        ArrayList<String> o = (ArrayList<String>) field.get(entity);
                        ArrayList<String> list = new ArrayList<>();
                        if (0 < o.size()) {


                            for (String s : o) {
                                if (StringUtils.isNotBlank(s)) {
                                    list.add(getRelativePath(s));
                                }
                            }
                            field.set(entity, list);
                            if (null != fillFiled) {
                                fillFiled.set(entity, StringUtils.join(list, anSplit));
                                ignoreField.add(anFieldKey);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 实体类中的属性值转换为绝对路径
     *
     * @param entity 实体类
     */
    public void convertPathToRel(Object entity) {
        try {
            Class<?> entityClass = entity.getClass();
            ArrayList<String> ignoreField = new ArrayList<>();
            for (Field field : entityClass.getDeclaredFields()) {
                if (ignoreField.contains(field.getName())) {
                    continue;
                }
                if (field.isAnnotationPresent(TxCosUrl.class)) {
                    TxCosUrl annotation = field.getAnnotation(TxCosUrl.class);
                    String anSplit = annotation.split();
                    String anFieldKey = annotation.fieldKey();
                    field.setAccessible(true);
                    Field fillFiled = null;
                    try {
                        //查询是否存在该字段
                        if (StringUtils.isNotBlank(anSplit) && StringUtils.isNotBlank(anFieldKey)) {
                            fillFiled = entityClass.getDeclaredField(anFieldKey);
                            fillFiled.setAccessible(true);
                        }

                    } catch (NoSuchFieldException ignored) {
                    }
                    if (String.class == field.getType()) {
                        String o = (String) field.get(entity);
                        if (null != fillFiled) {
                            String[] split = StringUtils.split(o, anSplit);
                            for (int i = 0; i < split.length; i++) {
                                split[i] = getCosObjectDownAuthUrl(split[i]);
                            }
                            fillFiled.set(entity, new ArrayList<>(Arrays.asList(split)));
                            ignoreField.add(anFieldKey);
                        } else {
                            field.setAccessible(true);
                            field.set(entity, getCosObjectDownAuthUrl(o));
                        }
                    } else if ("java.util.List<java.lang.String>".equals(String.valueOf(field.getGenericType()))) {
                        ArrayList<String> o = (ArrayList<String>) field.get(entity);
                        ArrayList<String> list = new ArrayList<>();
                        if (0 < o.size()) {
                            for (String s : o) {
                                if (StringUtils.isNotBlank(s)) {
                                    list.add(getCosObjectDownAuthUrl(s));
                                }
                            }
                            field.set(entity, list);
                            if (null != fillFiled) {
                                fillFiled.set(entity, StringUtils.join(list, anSplit));
                                ignoreField.add(anFieldKey);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
        }
    }

}

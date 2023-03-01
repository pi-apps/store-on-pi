package com.api.service.sdk.TxCosSDK.cos;


import com.api.service.sdk.TxCosSDK.CosConfig;
import lombok.Data;


@Data
public class CosPublicEntity {

    private String appId;

    private String secretId;

    private String secretKey;

    private String regionName;

    private String bucketName;

    private String objectKey;

    public static CosPublicEntity build(String objectKey) {
        CosPublicEntity cosPublicEntity = new CosPublicEntity();
        cosPublicEntity.setAppId(CosConfig.appId);
        cosPublicEntity.setSecretId(CosConfig.secretId);
        cosPublicEntity.setSecretKey(CosConfig.secretKey);
        cosPublicEntity.setRegionName(CosConfig.regionName);
        cosPublicEntity.setBucketName(CosConfig.bucketName);
        cosPublicEntity.setObjectKey(objectKey);
        return cosPublicEntity;

    }

}

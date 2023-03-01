package com.api.service.sdk.TxSmsSdk;

import com.api.service.sdk.TxCosSDK.CosConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

/**
 * Tencent Cloud Sms Sendsms
 */
public class TxSendSms {
    private static SmsClient client;
    /**
     * 发送短信
     *
     * @param moblie 手机号码
     * @param type   短信类型 : [1]通知验证码 [2]还没有
     * @param msg1   内容1
     * @param msg2   内容2
     */
    public static boolean sendSms(String moblie, String type, String msg1, String msg2) {
        if (null == client) {
            Credential cred = new Credential(CosConfig.secretId, CosConfig.secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setReqMethod("POST");
            httpProfile.setConnTimeout(60);
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            client = new SmsClient(cred, "ap-guangzhou", clientProfile);
        }
        switch (type) {
            case "1":
                return send_1488223(client, moblie, msg1);
            default:
                throw new RuntimeException("类型错误");
        }

    }

    /**
     * 通知短信
     *
     * @param sct
     * @param moblie
     * @param msg
     * @return
     */
    private static boolean send_1488223(SmsClient sct, String moblie, String msg) {
        try {
            SendSmsRequest req = new SendSmsRequest();
            String sdkAppId = "1400712244";
            req.setSmsSdkAppId(sdkAppId);
            String signName = "天天车宝";
            req.setSignName(signName);
            String templateId = "1488223";
            req.setTemplateId(templateId);
            String[] templateParamSet = {msg};
            req.setTemplateParamSet(templateParamSet);

            String[] phoneNumberSet = {"+86" + moblie};
            req.setPhoneNumberSet(phoneNumberSet);
            String sessionContext = "";
            req.setSessionContext(sessionContext);
            String extendCode = "";
            req.setExtendCode(extendCode);
            String senderid = "";
            req.setSenderId(senderid);
            SendSmsResponse res = sct.SendSms(req);
            String code = res.getSendStatusSet()[0].getCode();
            if ("Ok".equals(code)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }


}

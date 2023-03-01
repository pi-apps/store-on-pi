package com.api.service.sms;

import com.api.service.RedisLockUtil;
import com.api.service.RedisUtil;
import com.api.service.UUID;
import com.api.service.sdk.TxSmsSdk.TxSendSms;
import com.api.service.serviceReturn.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Api(tags = {"短信验证码获取"}, description = "短信", position = 2)
@RestController
@RequestMapping("/cert/sms")
public class SmsController {

    private static final String redis_key = "cert_sms:";
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "获取短信验证码")
    @PostMapping("/getSms")
    public R<String> getSms(@RequestBody String mobile) {
        String lockName = "SmsController-getSms:" + mobile;
        String lockId = UUID.randomUUID();
        try {
            if (RedisLockUtil.tryLock(lockName, lockId, 60L)) {
                //60秒内不会重复发短信
                String code = (int) ((Math.random() * 9 + 1) * 1000) + "";
                System.out.println(code);
                redisUtil.redisTemplate().opsForValue().set(redis_key + mobile, code);
                redisUtil.redisTemplate().expire(redis_key + mobile, 5, TimeUnit.MINUTES);
                TxSendSms.sendSms(mobile, "1", code, null);
                return new R(0, "验证码发送成功!", null);
            } else {
                return new R(1, "请勿频繁操作", null);
            }
        } catch (Exception e) {
            return new R(1, "当天发送验证码已达到上限", null);
        }


    }


}

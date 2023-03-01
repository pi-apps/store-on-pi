package com.api.service.unAuth.access;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.api.service.RedisLockUtil;
import com.api.service.UUID;
import com.api.service.config.CommonConfig;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.entity.TMerStoreGoodsInfoEntity;
import com.api.service.db.entity.TMerStoreGoodsOrderEntity;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.db.mysql.service.ITMerStoreGoodsInfoService;
import com.api.service.db.mysql.service.ITMerStoreGoodsOrderService;
import com.api.service.http.HttpClientUtil;
import com.api.service.jwt.JwtController;
import com.api.service.serviceReturn.R;
import com.api.service.unAuth.access.dto.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Api(tags = {"PI后端API部分"}, position = 100)
@RestController
@RequestMapping("/app/unAuth/access")
public class AccessController extends JwtController {


    @Autowired
    private ITApiUserService itApiUserService;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private ITMerStoreGoodsOrderService itMerStoreGoodsOrderService;

    @Autowired
    private ITMerStoreGoodsInfoService itMerStoreGoodsInfoService;


    @ApiOperation(value = "PI用户登录验权,获得token(header带上token)", position = 1)
    @PostMapping("/me")
    public R<TApiUserEntity> me(@RequestBody LoginDto loginDto) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.minepi.com/v2/me")
                    .addHeader("Authorization", "Bearer " + loginDto.getAccessToken())
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("login error " + response.body().string());
                }
                String string = response.body().string();
                JSONObject jsonObject1 = JSON.parseObject(string);
                if (!jsonObject1.getString("uid").equals(loginDto.getUid())) {
                    throw new RuntimeException("auth error " + response.body().string());
                }

                TApiUserEntity currentUser = itApiUserService.selectById(loginDto.getUid());
                if (null != currentUser) {
                    //如果用户存在更新token
                    if (!currentUser.getAccessToken().equals(loginDto.getAccessToken())) {
                        currentUser.setAccessToken(loginDto.getAccessToken());
                        itApiUserService.updateById(currentUser);
                    }
                } else {
                    //新用户
                    currentUser = new TApiUserEntity();
                    currentUser.setUserId(loginDto.getUid());
                    currentUser.setAccessToken(loginDto.getAccessToken());
                    currentUser.setUserName(loginDto.getUserName());
                    currentUser.setIsStore("1");//非门店
                    currentUser.setStatus("0");//启用
                    currentUser.setInvitation(loginDto.getUserName());//邀请码就是PI用户名


                    itApiUserService.insert(currentUser);
                    currentUser = itApiUserService.selectById(loginDto.getUid());
                }
                return new R(0, "获取成功", currentUser);
            }
        } catch (Exception e) {
            return new R(1, fail(e), null);
        }
    }

    @ApiOperation(value = "处理未完成的订单", position = 2)
    @PostMapping("/incomplete")
    public R<TApiUserEntity> incomplete(@RequestBody IncompleteDto payment) {
        try {
            //先处理未完成的订单
            String oldpaymentId = payment.getIdentifier();
            TransactionDto transaction = payment.getTransaction();
            if (null != transaction) {
                String txid = transaction.getTxid();
                String txURL = transaction.get_link();

                Wrapper<TMerStoreGoodsOrderEntity> wrappe1 = new EntityWrapper<TMerStoreGoodsOrderEntity>()
                        .where("1=1")
                        .eq("pay_order_id", oldpaymentId);
                TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity = itMerStoreGoodsOrderService.selectOne(wrappe1);
                if (null == tMerStoreGoodsOrderEntity) {
                    throw new RuntimeException("旧订单不存在");
                }
                String payStatusType = tMerStoreGoodsOrderEntity.getPayStatusType();
                if ("10007003".equals(payStatusType)) {
                    throw new RuntimeException("订单是已支付状态");
                }

                String get = HttpClientUtil.sendGet(txURL);
                JSONObject jsonObject1 = JSON.parseObject(get);
                String piOrderId = jsonObject1.getString("memo");//我方订单ID
                if (!piOrderId.equals(tMerStoreGoodsOrderEntity.getPayOrderId())) {
                    throw new RuntimeException("数据库中旧订单不存在:" + piOrderId);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("txid", txid);

                Map<String, String> heads = new HashMap<>();
                heads.put("Content-Type", "application/json;charset=UTF-8");
                heads.put("Authorization", "Key " + commonConfig.getServerAccessKey());

                try {
                    HttpResponse response = HttpRequest.post("https://api.minepi.com/v2/payments/" + piOrderId + "/complete")
                            .headerMap(heads, false)
                            .body(String.valueOf(jsonObject))
                            .timeout(5 * 60 * 1000)
                            .execute();
                    String body = response.body();
                    JSONObject jsonObject2 = JSON.parseObject(body);
                    String error = jsonObject2.getString("error");
                    if (!StringUtils.isEmpty(error)) {
                        throw new RuntimeException("订单完成异常!");
                    }
                    tMerStoreGoodsOrderEntity.setTxId(txid);
                    tMerStoreGoodsOrderEntity.setPayStatusType("10007003");//支付成功
                    tMerStoreGoodsOrderEntity.setPayTime(new Date());
                    tMerStoreGoodsOrderEntity.setIsUse("1");//未使用
                    tMerStoreGoodsOrderEntity.setPayChannelType("0");//PI钱包
                    itMerStoreGoodsOrderService.updateById(tMerStoreGoodsOrderEntity);
                    return new R(0, "处理成功", null);
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            return new R(1, fail(e), null);
        }
        return null;
    }

    @ApiOperation(value = "前端请求支付授权,在本地订单创建后调", position = 3)
    @PostMapping("/approve")
    public R<TApiUserEntity> approve(@RequestBody PaymentDto paymentDto) {
        String orderId = paymentDto.getOrderId();//本地订单号
        TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity = itMerStoreGoodsOrderService.selectById(orderId);
        if (null == tMerStoreGoodsOrderEntity) {
            return new R(1, "订单不存在", null);
        }
        String payStatusType = tMerStoreGoodsOrderEntity.getPayStatusType();
        if (!"10007001".equals(payStatusType)) {
            return new R(1, "订单不是待支付状态", null);
        }

        //获取付款信息
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.minepi.com/v2/payments/" + paymentDto.getPaymentId())
                .addHeader("Authorization", "Key " + commonConfig.getServerAccessKey())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String string = response.body().string();
                JSONObject jsonObject1 = JSON.parseObject(string);
                throw new RuntimeException("payments error " + jsonObject1.getString("error_message"));
            }
            String string = response.body().string();
            JSONObject jsonObject1 = JSON.parseObject(string);
            //校验实际支付金额
            BigDecimal userFinalPrice = tMerStoreGoodsOrderEntity.getUserFinalPrice();

            if (userFinalPrice.compareTo(jsonObject1.getBigDecimal("amount")) > 0) {
                throw new RuntimeException("支付金额少于订单金额");
            }
            //信息真实，通知PI我准备好了，可以付款了
            Request request1 = new Request.Builder()
                    .url("https://api.minepi.com/v2/payments/" + paymentDto.getPaymentId() + "/approve")
                    .addHeader("Authorization", "Key " + commonConfig.getServerAccessKey()).post(okhttp3.RequestBody.create(null, ""))
                    .build();
            try (Response response1 = client.newCall(request1).execute()) {
                if (!response1.isSuccessful()) {
                    String string2 = response1.body().string();
                    JSONObject jsonObject2 = JSON.parseObject(string2);
                    throw new RuntimeException("approve error: " + jsonObject2.get("error_message"));
                }
                //更新支付报文
                tMerStoreGoodsOrderEntity.setPayOrderId(paymentDto.getPaymentId());
                tMerStoreGoodsOrderEntity.setPayStatusType("10007002");//支付中
                itMerStoreGoodsOrderService.updateById(tMerStoreGoodsOrderEntity);
            } catch (RuntimeException e) {
                throw e;
            }
            return new R(0, "success", null);
        } catch (Exception e) {
            return new R(1, fail(e), null);
        }
    }

    @ApiOperation(value = "前端支付完成,余额支付直接调用此方法", position = 4)
    @PostMapping("/complete")
    public R<TApiUserEntity> complete(@RequestBody CompleteDto completeDto) {
        String payType = completeDto.getPayType();
        if ("1".equals(payType)) {
            //余额支付
            String orderId = completeDto.getOrderId();
            String lockName = "access:lock:complete:" + orderId;
            String lockId = UUID.randomUUID();
            if (!RedisLockUtil.tryLock(lockName, lockId, 20L)) {
                return new R(1, "调用太快", null);
            }
            TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity = itMerStoreGoodsOrderService.selectById(orderId);
            if (!"10007001".equals(tMerStoreGoodsOrderEntity.getPayStatusType())) {
                return new R(1, "订单不是待支付状态", null);
            }
            String userId = getUserId();
            TApiUserEntity tApiUserEntity = itApiUserService.selectById(userId);
            BigDecimal balance = tApiUserEntity.getBalance();
            if (balance.compareTo(tMerStoreGoodsOrderEntity.getUserFinalPrice()) < 0) {
                return new R(1, "余额不足,请前往充值", null);
            }
            itMerStoreGoodsOrderService.pay(tMerStoreGoodsOrderEntity);
            return new R(0, "支付成功", null);
        }
        //PI钱包支付
        String paymentId = completeDto.getPaymentId();//PI订单号
        String lockName = "access:lock:complete:" + paymentId;
        String lockId = UUID.randomUUID();
        if (!RedisLockUtil.tryLock(lockName, lockId, 20L)) {
            return new R(1, "调用太快", null);
        }
        Wrapper<TMerStoreGoodsOrderEntity> wrapper = new EntityWrapper<TMerStoreGoodsOrderEntity>()
                .where("1=1")
                .eq("pay_order_id", paymentId);
        TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity = itMerStoreGoodsOrderService.selectOne(wrapper);
        if (null == tMerStoreGoodsOrderEntity) {
            return new R(1, "订单不存在", null);
        }
        String payStatusType = tMerStoreGoodsOrderEntity.getPayStatusType();
        if (!"10007002".equals(payStatusType)) {
            return new R(1, "订单不是支付中状态", null);
        }


        //通知PI完成交易
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txid", completeDto.getTxId());

        Map<String, String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json;charset=UTF-8");
        heads.put("Authorization", "Key " + commonConfig.getServerAccessKey());

        try {
            HttpResponse response = HttpRequest.post("https://api.minepi.com/v2/payments/" + paymentId + "/complete")
                    .headerMap(heads, false)
                    .body(String.valueOf(jsonObject))
                    .timeout(5 * 60 * 1000)
                    .execute();
            String body = response.body();
            JSONObject jsonObject1 = JSON.parseObject(body);
            String error = jsonObject1.getString("error");
            if (!StringUtils.isEmpty(error)) {
                throw new RuntimeException("订单完成异常!");
            }
            tMerStoreGoodsOrderEntity.setTxId(completeDto.getTxId());
            tMerStoreGoodsOrderEntity.setPayStatusType("10007003");//支付成功
            tMerStoreGoodsOrderEntity.setPayTime(new Date());
            tMerStoreGoodsOrderEntity.setIsUse("1");//未使用
            tMerStoreGoodsOrderEntity.setPayChannelType("0");//PI钱包
            itMerStoreGoodsOrderService.updateById(tMerStoreGoodsOrderEntity);
            return new R(0, "success", null);
        } catch (Exception e) {
            throw e;
        }
    }

    @ApiOperation(value = "取消支付,订单关闭", position = 4)
    @PostMapping("/cancelledPayment")
    public R<TApiUserEntity> cancelledPayment(@RequestBody String paymentId) {

        Wrapper<TMerStoreGoodsOrderEntity> wrapper = new EntityWrapper<TMerStoreGoodsOrderEntity>()
                .where("1=1")
                .eq("pay_order_id", paymentId);
        TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity = itMerStoreGoodsOrderService.selectOne(wrapper);
        String payStatusType = tMerStoreGoodsOrderEntity.getPayStatusType();
        if (!"10007001".equals(payStatusType) && !"10007002".equals(payStatusType)) {
            return new R(1, "当前订单状态不能取消", null);
        }
        //恢复商品库存
        TMerStoreGoodsInfoEntity goodsInfoEntity = JSON.parseObject(tMerStoreGoodsOrderEntity.getGoodsJson(), new TypeReference<TMerStoreGoodsInfoEntity>() {
        });
        StringBuilder builder = new StringBuilder();
        builder.append("inventory = inventory + 1");
        itMerStoreGoodsInfoService.updateForSet(builder.toString(), new EntityWrapper<TMerStoreGoodsInfoEntity>().eq("id", goodsInfoEntity.getId()));

        tMerStoreGoodsOrderEntity.setPayStatusType("10007005");//订单关闭
        itMerStoreGoodsOrderService.updateById(tMerStoreGoodsOrderEntity);
        return new R(0, "success", null);
    }
}

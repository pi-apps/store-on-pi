package com.api.service.db.mysql.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.api.service.UUID;
import com.api.service.db.entity.TApiUserAccountChangeEntity;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.mapper.TMerStoreGoodsOrderMapper;
import com.api.service.db.mysql.service.ITApiUserAccountChangeService;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.db.mysql.service.ITMerStoreGoodsOrderService;
import com.api.service.sdk.TxCosSDK.TxCosUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("ITMerStoreGoodsOrderService")
public class TMerStoreGoodsOrderServiceImpl extends ServiceImpl<TMerStoreGoodsOrderMapper, TMerStoreGoodsOrderEntity> implements ITMerStoreGoodsOrderService {


    @Autowired
    private TxCosUtil txCosUtil;
    @Autowired
    private ITApiUserService itApiUserService;
    @Autowired
    private ITApiUserAccountChangeService itApiUserAccountChangeService;//用户账目

    /**
     * 快照转换
     *
     * @param entity
     */
    @Override
    public void convertEntityCos(TMerStoreInfoEntity entity) {
        /**
         * 图片路径处理
         */
        if (StringUtils.isNotBlank(entity.getDoorPhotoUrl())) {
            entity.setDoorPhotoUrl(txCosUtil.getCosObjectDownAuthUrl(entity.getDoorPhotoUrl()));
        }
        if (StringUtils.isNotBlank(entity.getPhotosUrl())) {
            String[] photos = entity.getPhotosUrl().split(",");
            List<String> stringList = new ArrayList<>();
            for (String photo : photos) {
                stringList.add(txCosUtil.getCosObjectDownAuthUrl(photo));
            }
            entity.setPhotosUrlList(stringList);
        }
        //短视频路径
        if (StringUtils.isNotBlank(entity.getDoorPhotoVideoUrl())) {
            entity.setDoorPhotoVideoUrl(txCosUtil.getCosObjectDownAuthUrl(entity.getDoorPhotoVideoUrl()));
        }
        //根据营业时间判断是否在营业中
        entity.setIsOpen("0");
        String openTimeStart = DateUtil.formatDate(new Date()) + " " + entity.getOpenTimeStart() + ":00";
        String openTimeEnd = DateUtil.formatDate(new Date()) + " " + entity.getOpenTimeEnd() + ":00";
        Date nowTime = new Date();

        DateTime openTimeStartTime = DateUtil.parseDateTime(openTimeStart);
        DateTime openTimeEndTime = DateUtil.parseDateTime(openTimeEnd);
        if (nowTime.compareTo(openTimeStartTime) < 0 || nowTime.compareTo(openTimeEndTime) > 0) {
            entity.setIsOpen("1"); //休息中
        }
    }

    @Override
    public void desensitizationEntity(TMerStoreGoodsOrderEntity entity) {
        entity.setSettlementPrice(BigDecimal.ZERO);
        entity.setSettlementRate(BigDecimal.ZERO);
        entity.setServicePrice(BigDecimal.ZERO);
        entity.setServiceRate(BigDecimal.ZERO);
        entity.setProfit(BigDecimal.ZERO);
        entity.setOrderExtendJson("");
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void refund(TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity) {
        tMerStoreGoodsOrderEntity.setRefundAmount(tMerStoreGoodsOrderEntity.getUserFinalPrice());
        tMerStoreGoodsOrderEntity.setRefundReason("用户主动申请退款");
        tMerStoreGoodsOrderEntity.setRefundTime(new Date());
        tMerStoreGoodsOrderEntity.setPayStatusType("10007006");//已退款
        tMerStoreGoodsOrderEntity.setIsUse("2");
        this.updateById(tMerStoreGoodsOrderEntity);
        //增加余额
        StringBuilder builder1 = new StringBuilder();
        builder1.append("balance = balance + " + tMerStoreGoodsOrderEntity.getUserFinalPrice());
        itApiUserService.updateForSet(builder1.toString(), new EntityWrapper<TApiUserEntity>().

                eq("user_id", tMerStoreGoodsOrderEntity.getApiUserId()));


        TApiUserAccountChangeEntity userAccountInfoEntity = new TApiUserAccountChangeEntity();
        userAccountInfoEntity.setId(UUID.randomUUID());
        userAccountInfoEntity.setUserName(tMerStoreGoodsOrderEntity.getApiUserName());//主键
        userAccountInfoEntity.setAmount(tMerStoreGoodsOrderEntity.getUserFinalPrice());//结算价
        userAccountInfoEntity.setRemark("订单退款,余额增加");
        userAccountInfoEntity.setOrderId(tMerStoreGoodsOrderEntity.getId());
        userAccountInfoEntity.setCreateTime(new Date());
        itApiUserAccountChangeService.insert(userAccountInfoEntity);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void pay(TMerStoreGoodsOrderEntity tMerStoreGoodsOrderEntity) {
        //更改订单状态
        tMerStoreGoodsOrderEntity.setPayStatusType("10007003");//支付成功
        tMerStoreGoodsOrderEntity.setPayTime(new Date());
        tMerStoreGoodsOrderEntity.setIsUse("1");//未使用
        tMerStoreGoodsOrderEntity.setPayChannelType("1");//余额支付
        this.updateById(tMerStoreGoodsOrderEntity);
        //扣除余额
        StringBuilder builder1 = new StringBuilder();
        builder1.append("balance = balance - " + tMerStoreGoodsOrderEntity.getUserFinalPrice());
        itApiUserService.updateForSet(builder1.toString(), new EntityWrapper<TApiUserEntity>().
                eq("user_id", tMerStoreGoodsOrderEntity.getApiUserId()));

        TApiUserAccountChangeEntity userAccountInfoEntity = new TApiUserAccountChangeEntity();
        userAccountInfoEntity.setId(UUID.randomUUID());
        userAccountInfoEntity.setUserName(tMerStoreGoodsOrderEntity.getApiUserName());//主键
        userAccountInfoEntity.setAmount(tMerStoreGoodsOrderEntity.getUserFinalPrice().multiply(new BigDecimal("-1")));//结算价
        userAccountInfoEntity.setRemark("余额支付,扣除余额");
        userAccountInfoEntity.setOrderId(tMerStoreGoodsOrderEntity.getId());
        userAccountInfoEntity.setCreateTime(new Date());
        itApiUserAccountChangeService.insert(userAccountInfoEntity);
    }

}

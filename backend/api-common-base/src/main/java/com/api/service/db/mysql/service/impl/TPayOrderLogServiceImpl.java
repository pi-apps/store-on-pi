package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TPayOrderLogEntity;
import com.api.service.db.mysql.mapper.TPayOrderLogMapper;
import com.api.service.db.mysql.service.ITPayOrderLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service("ITPayOrderLogService")
public class TPayOrderLogServiceImpl extends ServiceImpl<TPayOrderLogMapper, TPayOrderLogEntity> implements ITPayOrderLogService {


    @Override
    public void inserEntity(String orderId, String businessLineType, String businessChannelType, String payChannelName, String payText, String payChannelType) {

        TPayOrderLogEntity tPayOrderLogEntity = new TPayOrderLogEntity();

        tPayOrderLogEntity.setOrderId(orderId);

        tPayOrderLogEntity.setBusinessChannelType(businessChannelType);

        tPayOrderLogEntity.setBusinessLineType(businessLineType);

        if (StringUtils.isBlank(payChannelName)) {
            tPayOrderLogEntity.setPayChannelName("10013002");
        } else {
            tPayOrderLogEntity.setPayChannelName(payChannelName);
        }

        tPayOrderLogEntity.setCreateTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        tPayOrderLogEntity.setCreateTimeStr(sdf.format(tPayOrderLogEntity.getCreateTime()));
        tPayOrderLogEntity.setPayChannelType(payChannelType);
        tPayOrderLogEntity.setPayText(payText);

        this.insert(tPayOrderLogEntity);


    }


}

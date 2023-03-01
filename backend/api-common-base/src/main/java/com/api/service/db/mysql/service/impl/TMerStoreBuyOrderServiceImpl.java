package com.api.service.db.mysql.service.impl;

import com.api.service.db.mysql.mapper.TMerStoreBuyOrderMapper;
import com.api.service.db.mysql.service.ITMerStoreBuyOrderService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


@Service("ITMerStoreBuyOrderService")
public class TMerStoreBuyOrderServiceImpl extends ServiceImpl<TMerStoreBuyOrderMapper, TMerStoreBuyOrderEntity> implements ITMerStoreBuyOrderService {


    @Override
    public TMerStoreBuyOrderEntity selectByThirdId(String thirdId) {
        if (StringUtils.isBlank(thirdId)) {
            throw new NullPointerException("thirdId is null");
        }
        Wrapper<TMerStoreBuyOrderEntity> wrapper = new EntityWrapper<TMerStoreBuyOrderEntity>()
                .where("1=1")
                .eq("third_id", thirdId);
        TMerStoreBuyOrderEntity selectEntity = this.selectOne(wrapper);

        return selectEntity;
    }
}

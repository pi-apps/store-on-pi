package com.api.service.db.mysql.service.impl;

import com.api.service.UUID;
import com.api.service.db.entity.TApiUserAccountChangeEntity;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.mapper.TCMSUserMapper;
import com.api.service.db.mysql.service.ITApiUserAccountChangeService;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.db.mysql.service.ITCMSUserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service("ITCMSUserService")
public class TCMSUserServiceImpl extends ServiceImpl<TCMSUserMapper, TCmsUserEntity> implements ITCMSUserService {
    @Autowired
    private ITApiUserService itApiUserService;
    @Autowired
    private ITApiUserAccountChangeService itApiUserAccountChangeService;//用户账目

    @Override
    public TCmsUserEntity selectByMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            throw new NullPointerException("mobile is null");
        }
        Wrapper<TCmsUserEntity> wrapper = new EntityWrapper<TCmsUserEntity>()
                .where("1=1")
                .eq("mobile", mobile);
        TCmsUserEntity selectEntity = this.selectOne(wrapper);

        return selectEntity;
    }

    @Override
    public Set<String> queryAllPerms(String userId) {
        List<String> p = this.baseMapper.queryAllPerms(userId);
        if (null == p || p.isEmpty()) {
            return null;
        }
        Set<String> permsSet = new HashSet<>();
        for (String s : p) {
            permsSet.addAll(Arrays.asList(s.trim().split(",")));
        }


        return permsSet;
    }

    @Override
    public List<String> queryAllMenuId(String userId) {
        List<String> p = this.baseMapper.queryAllMenuId(userId);
        if (null == p || p.isEmpty()) {
            return null;
        }
        return p;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void convert(String cmsUserId, String apiuserId, BigDecimal amount) {
        //商家余额减少
        StringBuilder builder2 = new StringBuilder();
        builder2.append("balance = balance - " + amount);
        this.updateForSet(builder2.toString(), new EntityWrapper<TCmsUserEntity>().eq("user_id", cmsUserId));

        //绑定的APP用户余额增多
        StringBuilder builder3 = new StringBuilder();
        builder3.append("balance = balance + " + amount);
        itApiUserService.updateForSet(builder3.toString(), new EntityWrapper<TApiUserEntity>().eq("user_id", apiuserId));

        //写APP变动表
        TApiUserEntity tApiUserEntity = itApiUserService.selectById(apiuserId);
        TApiUserAccountChangeEntity userAccountInfoEntity = new TApiUserAccountChangeEntity();
        userAccountInfoEntity.setId(UUID.randomUUID());
        userAccountInfoEntity.setUserName(tApiUserEntity.getUserName());
        userAccountInfoEntity.setAmount(amount);//结算价
        userAccountInfoEntity.setRemark("商家余额转换");
        userAccountInfoEntity.setOrderId(null);
        userAccountInfoEntity.setCreateTime(new Date());
        itApiUserAccountChangeService.insert(userAccountInfoEntity);
    }


}

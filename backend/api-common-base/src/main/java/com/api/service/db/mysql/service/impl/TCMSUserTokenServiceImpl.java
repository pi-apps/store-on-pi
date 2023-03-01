package com.api.service.db.mysql.service.impl;

import com.api.service.db.mysql.mapper.TCMSUserTokenMapper;
import com.api.service.db.mysql.service.ITCMSUserTokenService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import com.api.service.UUID;;


@Service("ITCMSUserTokenService")
public class TCMSUserTokenServiceImpl extends ServiceImpl<TCMSUserTokenMapper, TCmsUserTokenEntity> implements ITCMSUserTokenService {


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String createToken(String userId, String token) {
        boolean ok;
        //生成一个token
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + 3600 * 24 * 1L * 1000);
        //判断是否生成过token
        TCmsUserTokenEntity tokenEntity = this.selectById(userId);
        if (tokenEntity == null) {
            tokenEntity = new TCmsUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            ok = this.insert(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //更新token
            ok = this.updateById(tokenEntity);
        }
        if (!ok) {
            return null;
        }
        return token;
    }


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void logout(String userId) {
        //修改token
        TCmsUserTokenEntity tokenEntity = new TCmsUserTokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(UUID.randomUUID());
        this.updateById(tokenEntity);
    }
}

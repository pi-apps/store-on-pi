package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.mapper.TApiUserMapper;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.db.mysql.util.PageUtils;
import com.api.service.db.mysql.util.Query;
import com.api.service.db.mysql.util.beanToMap.BeanMapUtil;
import com.api.service.sdk.TxCosSDK.TxCosUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("ITApiUserService")
public class TApiUserServiceImpl extends ServiceImpl<TApiUserMapper, TApiUserEntity> implements ITApiUserService {


    @Autowired
    private TxCosUtil txCosUtil;


    @Override
    public TApiUserEntity selectByMobile(String mobile) {
        Wrapper<TApiUserEntity> wrapper = new EntityWrapper<TApiUserEntity>()
                .where("1=1")
                .eq("mobile", mobile);
        TApiUserEntity selectEntity = this.selectOne(wrapper);
        this.extendEntity(selectEntity);
        return selectEntity;
    }

    @Override
    public TApiUserEntity selectByWxMiniOpenid(String wxMiniOpenid) {
        Wrapper<TApiUserEntity> wrapper = new EntityWrapper<TApiUserEntity>()
                .where("1=1")
                .eq("login_wx_opendid", wxMiniOpenid);
        TApiUserEntity selectEntity = this.selectOne(wrapper);
        this.extendEntity(selectEntity);
        return selectEntity;
    }

    @Override
    public TApiUserEntity selectByWxUnionid(String unionid) {
        Wrapper<TApiUserEntity> wrapper = new EntityWrapper<TApiUserEntity>()
                .where("1=1")
                .eq("login_wx_unionid", unionid);
        TApiUserEntity selectEntity = this.selectOne(wrapper);
        this.extendEntity(selectEntity);
        return selectEntity;
    }

    @Override
    public PageUtils selectByEntity(TApiUserEntity entity) {

        Wrapper<TApiUserEntity> wrapper = new EntityWrapper<TApiUserEntity>()
                .where("1=1")
                .like(StringUtils.isNotBlank(entity.getNickName()), "nick_name", entity.getNickName())
                .like(StringUtils.isNotBlank(entity.getUserName()), "user_name", entity.getUserName())
                .orderBy("create_time desc");
        Page<TApiUserEntity> page = this.selectPage(new Query<TApiUserEntity>(BeanMapUtil.toPageQuery(entity)).getPage(), wrapper);
        for (TApiUserEntity selectEntity : page.getRecords()) {
            this.extendEntity(selectEntity);

        }
        return new PageUtils(page);
    }

    /**
     * 功能扩展
     *
     * @param entity
     */
    private void extendEntity(TApiUserEntity entity) {
        if (null == entity) {
            return;
        }

    }
}

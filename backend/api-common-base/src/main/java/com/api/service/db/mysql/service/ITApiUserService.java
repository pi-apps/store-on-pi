package com.api.service.db.mysql.service;

import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.util.PageUtils;
import com.baomidou.mybatisplus.service.IService;


public interface ITApiUserService extends IService<TApiUserEntity> {
    TApiUserEntity selectByMobile(String mobile);

    TApiUserEntity selectByWxMiniOpenid(String mobile);

    TApiUserEntity selectByWxUnionid(String unionid);


    PageUtils selectByEntity(TApiUserEntity entity);


}

package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.service.IService;


public interface ITCMSUserTokenService extends IService<TCmsUserTokenEntity> {


//    TCmsUserTokenEntity queryByToken(String userId,String token);

    /**
     * 生成token
     *
     * @param userId 用户ID
     */
    String createToken(String userId, String token);

    /**
     * 退出，修改token值
     *
     * @param userId 用户ID
     */
    void logout(String userId);
}

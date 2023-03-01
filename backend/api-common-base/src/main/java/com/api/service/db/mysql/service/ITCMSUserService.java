package com.api.service.db.mysql.service;

import com.baomidou.mybatisplus.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


public interface ITCMSUserService extends IService<TCmsUserEntity> {

    TCmsUserEntity selectByMobile(String mobile);

    Set<String> queryAllPerms(String userId);

    List<String> queryAllMenuId(String userId);

    void convert(String cmsUserId, String apiuserId, BigDecimal amount);


//
//    /**
//     * 根据手机号，查询系统用户
//     */
//    TCmsUserEntity queryByUserMobile(String mobile);
//
//
//    PageUtils queryPage(TCmsUserEntity entity);
//
//    /**
//     * 保存用户
//     */
//    void save(TCmsUserEntity user);
//
//    /**
//     * 修改用户
//     */
//    void update(TCmsUserEntity user);
//
//    /**
//     * 删除用户
//     */
//    void deleteBatch(String[] userIds);
//
//    TCmsUserEntity queryByUserId(String userId);
//
//    /**
//     * 扩展用户辅助信息,不可轻易调用
//     *
//     * @param selectEntity
//     */
//    void extendUserEntity(TCmsUserEntity selectEntity);
//
//    /**
//     * 查询用户统计数量及占比
//     *
//     * @return
//     */
//    ViewCountQuantityProportionEntity countQuantityProportion();
//    /**
//     * 有效用户数增加,用户第一次进行充值调用,请勿每次调用
//     *
//     * @param selectEntity
//     */
//    void updatePidTotalEffectiveSubordinate(TCmsUserEntity selectEntity);

}

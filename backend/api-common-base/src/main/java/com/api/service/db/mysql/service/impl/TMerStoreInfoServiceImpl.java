package com.api.service.db.mysql.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.mapper.TMerStoreInfoMapper;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.db.mysql.service.ITMerStoreInfoService;
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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("ITMerStoreInfoService")
public class TMerStoreInfoServiceImpl extends ServiceImpl<TMerStoreInfoMapper, TMerStoreInfoEntity> implements ITMerStoreInfoService {

    @Autowired
    private TxCosUtil txCosUtil;

    @Autowired
    private ITApiUserService itApiUserService;


    @Override
    public Page<TMerStoreInfoEntity> getStoreList(TMerStoreInfoEntity entity) {
        Wrapper<TMerStoreInfoEntity> wrappe = new EntityWrapper<TMerStoreInfoEntity>()
                .where("1=1")
                .like(StringUtils.isNotBlank(entity.getStoreName()), "store_name", entity.getStoreName()) //查询门店信息
                .eq(StringUtils.isNotBlank(entity.getSysStatus()), "sys_status", entity.getSysStatus())
                .eq(StringUtils.isNotBlank(entity.getMobile()), "mobile", entity.getMobile())
                .eq(StringUtils.isNotBlank(entity.getAuthTagId()), "auth_tag_id", entity.getAuthTagId())
                .eq(StringUtils.isNotBlank(entity.getCmsUserId()), "cms_user_id", entity.getCmsUserId())
                .orderBy("sys_status ASC,update_time DESC,create_time DESC");
        Page<TMerStoreInfoEntity> page = this.selectPage(new Query<TMerStoreInfoEntity>(BeanMapUtil.toPageQuery(entity)).getPage(), wrappe);
        for (TMerStoreInfoEntity e : page.getRecords()) {

            /**
             * 图片处理
             */
            this.convertEntityCos(e);

            //推荐人信息
            if (StringUtils.isNotBlank(e.getInviterApiUserId())) {
                TApiUserEntity tApiUserEntity = itApiUserService.selectById(e.getInviterApiUserId());
                e.setInviterUser(tApiUserEntity);
            }
        }

        return page;
    }

    @Override
    public Page<TMerStoreInfoEntity> getPage(TMerStoreInfoEntity entity) {
        entity.setSearchScope(1000000);//默认1000公里
        /**
         * 与实体类对应的sql语句
         */
        Wrapper<TMerStoreInfoEntity> wrapper = new EntityWrapper<>();
        wrapper.where("1=1");
        wrapper.like(StringUtils.isNotBlank(entity.getStoreName()), "store_name", entity.getStoreName());
//        wrapper.eq(StringUtils.isNotBlank(entity.getAuthTagId()), "auth_tag_id", entity.getAuthTagId());
        wrapper.and("`auth_tag_id` IS NULL");
        wrapper.ne("`sys_status`", "1");//不等于1的门店全部放出来,兼容老数据
        wrapper.eq("`status`", "0"); //只展示上线的门店
        wrapper.eq(StringUtils.isNotBlank(entity.getStoreType()), "`store_type`", entity.getStoreType()); //门店分类
        //搜索半径条件
        wrapper.and(TMerStoreInfoEntity.andSql(entity.getSearchScope(), entity.getLocation()));
        //查询半径字段
        wrapper.setSqlSelect(TMerStoreInfoEntity.selectSql(entity.getSearchScope(), entity.getLocation()));
        //排序
        wrapper.orderBy("distance ASC,level ASC");
        Page<TMerStoreInfoEntity> page = this.selectPage(new Query<TMerStoreInfoEntity>(BeanMapUtil.toPageQuery(entity)).getPage(), wrapper);
        List<TMerStoreInfoEntity> records = page.getRecords();

        List<TMerStoreInfoEntity> newList = new ArrayList<>();

//        2022.10.25 屏蔽
//        if (null != topEntity) {
//            newList.add(topEntity);
//        }

        if (!CollectionUtils.isEmpty(records)) {
            for (TMerStoreInfoEntity e : page.getRecords()) {
                this.convertEntityCos(e);
                newList.add(e);
            }
            page.setRecords(newList);
        }
        return page;
    }

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
    public void desensitizationEntity(TMerStoreInfoEntity entity) {

        entity.setServiceRate(BigDecimal.ZERO);//收银台
    }
}

package com.api.service.db.mysql.service.impl;

import com.api.service.db.mysql.mapper.TMerStoreGoodsInfoMapper;
import com.api.service.db.mysql.service.ITMerStoreGoodsInfoService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service("ITMerStoreGoodsInfoService")
public class TMerStoreGoodsInfoServiceImpl extends ServiceImpl<TMerStoreGoodsInfoMapper, TMerStoreGoodsInfoEntity> implements ITMerStoreGoodsInfoService {

    @Autowired
    private TxCosUtil txCosUtil;

    @Override
//    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveGoods(TMerStoreGoodsInfoEntity inserEntity) {
        this.insert(inserEntity);
    }

    /**
     * 门店/内管查询门店商品
     *
     * @param entity
     * @return
     */
    @Override
    public Page<TMerStoreGoodsInfoEntity> getStoreGoodsList(TMerStoreGoodsInfoEntity entity) {

        Wrapper<TMerStoreGoodsInfoEntity> wrappe = new EntityWrapper<TMerStoreGoodsInfoEntity>()
                .where("1=1")
                .eq(StringUtils.isNotBlank(entity.getId()), "id", entity.getId())//商品主键
                .eq(StringUtils.isNotBlank(entity.getStoreId()), "store_id", entity.getStoreId())//门店主键
                .eq(StringUtils.isNotBlank(entity.getGoodsServiceId()), "goods_service_id", entity.getGoodsServiceId())//门店主键
                .eq(StringUtils.isNotBlank(entity.getGoodsStatus()), "goods_status", entity.getGoodsStatus())//商品状态
                .like(StringUtils.isNotBlank(entity.getGoodsName()), "goods_name", entity.getGoodsName()) //商品名称
                .orderBy("level ASC,update_time DESC,insert_time DESC");
        Page<TMerStoreGoodsInfoEntity> page = this.selectPage(new Query<TMerStoreGoodsInfoEntity>(BeanMapUtil.toPageQuery(entity)).getPage(), wrappe);
        for (TMerStoreGoodsInfoEntity e : page.getRecords()) {
            this.convertEntityCos(e);

        }
        return page;
    }

    /**
     * 用户客户端
     *
     * @param entity
     * @param platformGoodss
     * @return
     */
    @Override
    public Page<TMerStoreGoodsInfoEntity> getStoreGoodsList(TMerStoreGoodsInfoEntity entity, String[] platformGoodss) {
        Wrapper<TMerStoreGoodsInfoEntity> wrappe = new EntityWrapper<>();
        wrappe.where("1=1")
                .eq("goods_status", "0")//商品状态上架
                .eq(StringUtils.isNotBlank(entity.getId()), "id", entity.getId())//商品主键
                .eq(StringUtils.isNotBlank(entity.getStoreId()), "store_id", entity.getStoreId())//门店主键
                .eq(StringUtils.isNotBlank(entity.getGoodsStatus()), "goods_status", entity.getGoodsStatus())//商品状态
                .like(StringUtils.isNotBlank(entity.getGoodsName()), "goods_name", entity.getGoodsName()) //商品名称
                .eq(StringUtils.isNotBlank(entity.getGoodsServiceId()), "goods_service_id", entity.getGoodsServiceId()) //商品分类
                .orderBy("level ASC");
        Page<TMerStoreGoodsInfoEntity> page = this.selectPage(new Query<TMerStoreGoodsInfoEntity>(BeanMapUtil.toPageQuery(entity)).getPage(), wrappe);
        for (TMerStoreGoodsInfoEntity e : page.getRecords()) {
            this.convertEntityCos(e);

        }
        return page;
    }

    /**
     * 快照转换
     *
     * @param entity
     */
    @Override
    public void convertEntityCos(TMerStoreGoodsInfoEntity entity) {
        //主图
        if (StringUtils.isNotBlank(entity.getGoodsBannerUrl())) {
            String[] srts = entity.getGoodsBannerUrl().split(",");
            List<String> stringList = new ArrayList<>();
            for (String s : srts) {
                stringList.add(txCosUtil.getCosObjectDownAuthUrl(s));
            }
            entity.setGoodsBannerUrlList(stringList);
        }
        //视频
        if (StringUtils.isNotBlank(entity.getGoodsBannerVideoUrl())) {
            entity.setGoodsBannerVideoUrl(txCosUtil.getCosObjectDownAuthUrl(entity.getGoodsBannerVideoUrl()));
        }
        //详情图
        if (StringUtils.isNotBlank(entity.getGoodsDetailsPhotosUrl())) {
            String[] srts = entity.getGoodsDetailsPhotosUrl().split(",");
            List<String> stringList = new ArrayList<>();
            for (String s : srts) {
                stringList.add(txCosUtil.getCosObjectDownAuthUrl(s));
            }
            entity.setGoodsDetailsPhotosUrllList(stringList);
        }
    }

    /**
     * 数据脱敏
     *
     * @param entity
     */
    @Override
    public void desensitizationEntity(TMerStoreGoodsInfoEntity entity) {
        entity.setSettlementPrice(BigDecimal.ZERO);
        entity.setSettlementRate(BigDecimal.ZERO);
        entity.setServicePrice(BigDecimal.ZERO);
        entity.setServiceRate(BigDecimal.ZERO);
        entity.setProfit(BigDecimal.ZERO);
    }

}
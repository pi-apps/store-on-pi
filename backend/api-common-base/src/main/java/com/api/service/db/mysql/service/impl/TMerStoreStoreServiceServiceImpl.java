package com.api.service.db.mysql.service.impl;

import com.api.service.UUID;
import com.api.service.db.mysql.mapper.TMerStoreServiceMapper;
import com.api.service.db.mysql.service.ITMerStoreServiceService;
import com.api.service.db.mysql.util.Query;
import com.api.service.db.mysql.util.beanToMap.BeanMapUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("ITMerStoreServiceService")
public class TMerStoreStoreServiceServiceImpl extends ServiceImpl<TMerStoreServiceMapper, TMerStoreServiceEntity> implements ITMerStoreServiceService {
    @Override
    public Page<TMerStoreServiceEntity> listService(TMerStoreServiceEntity entity) {
        Wrapper<TMerStoreServiceEntity> wrapper = new EntityWrapper<TMerStoreServiceEntity>()
                .where("1=1")
                .eq(StringUtils.isNotBlank(entity.getStatus()), "status", entity.getStatus())
                .like(StringUtils.isNotBlank(entity.getId()), "id", entity.getId())
                .like(StringUtils.isNotBlank(entity.getStoreId()), "store_id", entity.getStoreId())
                .orderBy("level asc");
        Page<TMerStoreServiceEntity> page = this.selectPage(new Query<TMerStoreServiceEntity>(BeanMapUtil.toPageQuery(entity)).getPage(), wrapper);
        return page;
    }

    @Override
    public void addName(TMerStoreServiceEntity entity) {
        entity.setId(UUID.randomUUID());
        entity.setStatus("0");
        entity.setCreateTime(new Date());

        this.insert(entity);
    }

    @Override
    public void negate(String id) {
        TMerStoreServiceEntity entity = this.selectById(id);
        String status = "0".equals(entity.getStatus()) ? "1" : "0";
        entity.setStatus(status);
        this.updateById(entity);
    }
}

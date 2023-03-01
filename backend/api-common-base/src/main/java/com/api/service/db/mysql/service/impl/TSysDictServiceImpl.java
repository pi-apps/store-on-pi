package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSysDictEntity;
import com.api.service.db.mysql.mapper.TSysDictMapper;
import com.api.service.db.mysql.service.ITSysDictService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("ITSysDictService")
public class TSysDictServiceImpl extends ServiceImpl<TSysDictMapper, TSysDictEntity> implements ITSysDictService {


    @Override
    public Map<String, List<TSysDictEntity>> queryByTypes(String[] types) {
        Map<String, List<TSysDictEntity>> map = new HashMap<>();
        for (String type : types) {
            Wrapper<TSysDictEntity> wrapper = new EntityWrapper<TSysDictEntity>()
                    .where("1=1")
                    .eq("status", "0")
                    .eq("`type`", type)
                    .orderBy("`type`")
                    .orderBy("`level` ASC");
            List<TSysDictEntity> entityList = this.selectList(wrapper);
            map.put(type, entityList);
        }
        return map;
    }

    @Override
    public Map<String, String> queryByType(String type) {
        Wrapper<TSysDictEntity> wrapper = new EntityWrapper<TSysDictEntity>()
                .where("1=1")
                .eq("status", "0")
                .eq("`type`", type)
                .orderBy("`type`")
                .orderBy("`level` ASC");
        List<TSysDictEntity> entityList = this.selectList(wrapper);
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < entityList.size(); i++) {
            result.put(entityList.get(i).getValue(), entityList.get(i).getLabel());
        }
        return result;
    }

    @Override
    public List<TSysDictEntity> selectByValue(String value) {

        Wrapper<TSysDictEntity> wrapper = new EntityWrapper<TSysDictEntity>()
                .where("1=1")
                .eq("status", "0")
                .eq("`p_dict_id`", value)
                .orderBy("`level` ASC");
        List<TSysDictEntity> entityList = this.selectList(wrapper);

        return entityList;
    }
}

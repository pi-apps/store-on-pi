package com.api.service.db.mysql.service;

import com.api.service.db.entity.TSysDictEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;


public interface ITSysDictService extends IService<TSysDictEntity> {

    Map<String, List<TSysDictEntity>> queryByTypes(String[] types);

    Map<String, String> queryByType(String type);


    List<TSysDictEntity> selectByValue(String value);
}

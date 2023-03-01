package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSysAdvertiseEntity;
import com.api.service.db.mysql.mapper.TSysAdvertiseMapper;
import com.api.service.db.mysql.service.ITSysAdvertiseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITSysAdvertiseService")
public class TSysAdvertiseServiceImpl extends ServiceImpl<TSysAdvertiseMapper, TSysAdvertiseEntity> implements ITSysAdvertiseService {


}

package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSysContentEntity;
import com.api.service.db.mysql.mapper.TSysContentMapper;
import com.api.service.db.mysql.service.ITSysContentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITSysContentService")
public class TSysContentServiceImpl extends ServiceImpl<TSysContentMapper, TSysContentEntity> implements ITSysContentService {


}

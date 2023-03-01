package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSysConfigEntity;
import com.api.service.db.mysql.mapper.TSysConfigMapper;
import com.api.service.db.mysql.service.ITSysConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITSysConfigService")
public class TSysConfigServiceImpl extends ServiceImpl<TSysConfigMapper, TSysConfigEntity> implements ITSysConfigService {


}

package com.api.service.db.mysql.service.impl;

import com.api.service.db.mysql.mapper.TMerStoreAccountChangeMapper;
import com.api.service.db.mysql.service.ITMerStoreAccountChangeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITMerStoreAccountChangeService")
public class TMerStoreAccountChangeServiceImpl extends ServiceImpl<TMerStoreAccountChangeMapper, TMerStoreAccountChangeEntity> implements ITMerStoreAccountChangeService {

}

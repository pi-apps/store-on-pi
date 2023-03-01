package com.api.service.db.mysql.service.impl;

import com.api.service.db.mysql.mapper.TMerStoreApplyMapper;
import com.api.service.db.mysql.service.ITMerStoreApplyService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITMerStoreApplyService")
public class TMerStoreApplyServiceImpl extends ServiceImpl<TMerStoreApplyMapper, TMerStoreApplyEntity> implements ITMerStoreApplyService {

}

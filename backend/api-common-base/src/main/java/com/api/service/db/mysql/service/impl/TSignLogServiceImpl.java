package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSignLogEntity;
import com.api.service.db.mysql.mapper.TSignLogMapper;
import com.api.service.db.mysql.service.ITSignLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITSignLogService")
public class TSignLogServiceImpl extends ServiceImpl<TSignLogMapper, TSignLogEntity> implements ITSignLogService {


}

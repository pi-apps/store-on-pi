package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSignDateConfigEntity;
import com.api.service.db.mysql.mapper.TSignDateConfigMapper;
import com.api.service.db.mysql.service.ITSignDateConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITSignDateConfigService")
public class TSignDateConfigServiceImpl extends ServiceImpl<TSignDateConfigMapper, TSignDateConfigEntity> implements ITSignDateConfigService {


}

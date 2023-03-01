package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSignBaseConfigEntity;
import com.api.service.db.mysql.mapper.TSignBaseConfigMapper;
import com.api.service.db.mysql.service.ITSignBaseConfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITSignBaseConfigService")
public class TSignBaseConfigServiceImpl extends ServiceImpl<TSignBaseConfigMapper, TSignBaseConfigEntity> implements ITSignBaseConfigService {


}

package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TApiUserAccountChangeEntity;
import com.api.service.db.mysql.mapper.TApiUserAccountChangeMapper;
import com.api.service.db.mysql.service.ITApiUserAccountChangeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITApiUserAccountChangeService")
public class TApiUserAccountChangeServiceImpl extends ServiceImpl<TApiUserAccountChangeMapper, TApiUserAccountChangeEntity> implements ITApiUserAccountChangeService {

}

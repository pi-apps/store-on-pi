package com.api.service.db.mysql.service.impl;

import com.api.service.db.entity.TSysAgreementEntity;
import com.api.service.db.mysql.mapper.TSysAgreementMapper;
import com.api.service.db.mysql.service.ITSysAgreementService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service("ITSysAgreementService")
public class TSysAgreementServiceImpl extends ServiceImpl<TSysAgreementMapper, TSysAgreementEntity> implements ITSysAgreementService {


}

package com.api.service.login.service.impl;

import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.login.service.ICertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("IAuthService")
public class CertServiceImpl implements ICertService {

    @Autowired
    private ITApiUserService itApiUserService;


    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void toApiRegister(TApiUserEntity tApiUserEntity) {
        itApiUserService.insert(tApiUserEntity);

    }
}

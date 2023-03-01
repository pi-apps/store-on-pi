package com.api.service.auth.sign.service;

import com.api.service.auth.sign.entity.AfterSignDto;
import com.api.service.db.entity.TSignLogEntity;
import com.api.service.db.mysql.util.PageUtils;

import java.math.BigDecimal;
import java.util.List;

public interface IApiSignService {


    List<TSignLogEntity> signLog(String userId);

    Integer signDays(String userId);

    Integer signTotalCount(String userId);

    PageUtils detail(TSignLogEntity entity);

    BigDecimal sign(String userId);

    BigDecimal afterSign(AfterSignDto signDto, String userId);
}

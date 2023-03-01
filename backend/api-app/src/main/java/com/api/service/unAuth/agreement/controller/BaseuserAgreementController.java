package com.api.service.unAuth.agreement.controller;


import com.api.service.db.entity.TSysAgreementEntity;
import com.api.service.db.mysql.service.ITSysAgreementService;
import com.api.service.serviceReturn.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = {"协议书"}, description = "协议书", position = 8)
@RequestMapping("/app/unAuth/agreement")
@RestController
public class BaseuserAgreementController {

    @Autowired
    private ITSysAgreementService itAgreementService;


    @ApiOperation(value = "获取协议书", notes = "1")
    @GetMapping("/getAgreement/{agreementId}")
    public R getAdvertise(@ApiParam(name = "agreementId", value = "协议书主键", required = true) @PathVariable("agreementId") String agreementId) {

        TSysAgreementEntity entity = itAgreementService.selectById(agreementId);
        if (null == entity) {
            return new R(1, "协议不存在", null);
        }
        if (!"0".equals(entity.getStatus())) {
            return new R(1, "未查找到协议", null);
        }
        return new R(0, "获取成功", entity);
    }

}

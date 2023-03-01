package com.api.service.auth.sign.controller;


import com.api.service.MapUtils;
import com.api.service.auth.sign.entity.AfterSignDto;
import com.api.service.auth.sign.service.IApiSignService;
import com.api.service.config.FanyiUtil;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.entity.TSignBaseConfigEntity;
import com.api.service.db.entity.TSignDateConfigEntity;
import com.api.service.db.entity.TSignLogEntity;
import com.api.service.db.mysql.service.ITSignBaseConfigService;
import com.api.service.db.mysql.service.ITSignDateConfigService;
import com.api.service.db.mysql.util.PageUtils;
import com.api.service.jwt.JwtController;
import com.api.service.serviceReturn.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = {"鉴权-连续签到功能"}, position = 8)
@RequestMapping("/app/auth/sign")
@RestController
public class ApiSignController extends JwtController {
    @Autowired
    private ITSignDateConfigService itSignDateConfigService;
    @Autowired
    private ITSignBaseConfigService itSignBaseConfigService;
    @Autowired
    private IApiSignService iApiSignService;
    @Autowired
    private FanyiUtil fanyiUtil;


    @ApiOperation(value = "获取每天基础签到积分和补签价格、签到说明", position = 1)
    @PostMapping("/signBaseScoreNum")
    public R<TSignBaseConfigEntity> signBaseScoreNum() {
        Wrapper<TSignBaseConfigEntity> wrapper = new EntityWrapper<TSignBaseConfigEntity>()
                .where("1=1")
                .eq("status", "0");
        TSignBaseConfigEntity t = itSignBaseConfigService.selectOne(wrapper);

        TApiUserEntity user = getUser();
        //根据用户选择的语言进行翻译
        String remark = fanyiUtil.fanyi(user, t.getRemark());
        t.setRemark(remark);
        return new R(0, "获取成功", t);
    }


    @ApiOperation(value = "签到配置查询", position = 2)
    @PostMapping("/signConfig")
    public R<List<TSignDateConfigEntity>> signConfig() {
        List<TSignDateConfigEntity> list = itSignDateConfigService.selectByMap(new MapUtils().put("status", "0"));
        if (!CollectionUtils.isEmpty(list)) {
            Wrapper<TSignBaseConfigEntity> wrapper = new EntityWrapper<TSignBaseConfigEntity>()
                    .where("1=1")
                    .eq("status", "0");
            TSignBaseConfigEntity t = itSignBaseConfigService.selectOne(wrapper);

            for (int i = 0; i < list.size(); i++) {
                TSignDateConfigEntity configEntity = list.get(i);
                BigDecimal scoreNum = configEntity.getScoreNum();
                scoreNum = scoreNum.add(t.getSignBaseScoreNum());
                configEntity.setScoreNum(scoreNum);
                list.set(i, configEntity);
            }
            return new R(0, "获取成功", list);
        }
        return new R(1, "未配置签到参数", null);
    }

    @ApiOperation(value = "查询签到记录(上周和本周", position = 3)
    @PostMapping("/signLog")
    public R<List<TSignLogEntity>> signLog() {
        List<TSignLogEntity> list = iApiSignService.signLog(getUserId());
        return new R(0, "获取成功", list);
    }


    @ApiOperation(value = "获取30天内已连续签到总天数", position = 4)
    @PostMapping("/signDays")
    public R<Integer> signDays() {
        Integer day = iApiSignService.signDays(getUserId());
        return new R(0, "获取成功", day);
    }

    @ApiOperation(value = "获取累计签到总天数", position = 5)
    @PostMapping("/signTotalCount")
    public R<Integer> signTotalCount() {
        Integer day = iApiSignService.signTotalCount(getUserId());
        return new R(0, "获取成功", day);
    }

    @ApiOperation(value = "点击签到")
    @PostMapping("/sign")
    public R<BigDecimal> sign() {
        try {
            BigDecimal scoreNum = iApiSignService.sign(getUserId());
            return new R(0, "签到成功", scoreNum);
        } catch (Exception e) {
            return new R(1, e.getMessage(), null);
        }
    }

    @ApiOperation(value = "补签,需点击广告后免费补签")
    @PostMapping("/afterSign")
    public R<BigDecimal> afterSign(@RequestBody AfterSignDto signDto) {
        try {
            BigDecimal scoreNum = iApiSignService.afterSign(signDto, getUserId());
            return new R(0, "补签成功", scoreNum);
        } catch (Exception e) {
            return new R(1, e.getMessage(), null);
        }
    }


    @ApiOperation(value = "查询签到积分变动记录", position = 1)
    @PostMapping("/detail")
    public R<Page<TSignLogEntity>> detail(@RequestBody TSignLogEntity signLogEntity) {
        try {
            signLogEntity.setUserId(getUserId());
            PageUtils pageUtils = iApiSignService.detail(signLogEntity);
            return new R(0, "成功", pageUtils);
        } catch (Exception e) {
            return new R(1, e.getMessage(), null);
        }
    }
}

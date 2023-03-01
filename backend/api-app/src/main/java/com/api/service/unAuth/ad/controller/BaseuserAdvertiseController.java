package com.api.service.unAuth.ad.controller;

import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.entity.TSysAdvertiseEntity;
import com.api.service.db.mysql.service.ITSysAdvertiseService;
import com.api.service.jwt.JwtController;
import com.api.service.sdk.TxCosSDK.TxCosUtil;
import com.api.service.serviceReturn.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Api(tags = {"广告获取"}, position = 100)
@RestController
@RequestMapping("/app/unAuth/ad")
public class BaseuserAdvertiseController extends JwtController {

    @Autowired
    private TxCosUtil txCosUtil;
    @Autowired
    private ITSysAdvertiseService itSysAdvertiseService;


    @ApiOperation(value = "获取企业广告", notes = "根据位置类型查广告")
    @GetMapping("/getAd/{adSiteType}")
    public R<List<TSysAdvertiseEntity>> getAdvertise(@ApiParam(name = "adSiteType", value = "类型", required = true) @PathVariable("adSiteType") String adSiteType) {
       //根据语言切换广告
        TApiUserEntity user = getUser();
        String language = "0";
        if (null != user) {
            String language1 = user.getLanguage();
            if (!"chinese_simplified".equals(language1) && !"chinese_traditional".equals(language1)) {
                language = "1";
            }
        }

        Wrapper<TSysAdvertiseEntity> wrapper = new EntityWrapper<TSysAdvertiseEntity>()
                .where("1=1")
                .eq("status", "0")
                .eq("ad_site_type", adSiteType)
                .eq("language", language)
                .and("IFNULL(recall_time,STR_TO_DATE('2999-12-30 00:00:00','%Y-%m-%d %H:%i:%s'))>NOW()");

        List<TSysAdvertiseEntity> list = itSysAdvertiseService.selectList(wrapper);
        List<TSysAdvertiseEntity> all = new ArrayList<>();
        for (TSysAdvertiseEntity entity : list) {
            if (null != entity && new Date().getTime() >= entity.getPublishTime().getTime() //当前时间大于上架时间
            ) {
                //转换广告图片url
                entity.setAdImgUrl(txCosUtil.getCosObjectDownAuthUrl(entity.getAdImgUrl()));
                all.add(entity);
            }
        }
        if (!all.isEmpty()) {
            all.sort(Comparator.comparing((TSysAdvertiseEntity h) -> (h.getLevel())));
        }
        return new R(0, "获取成功", all);
    }


}

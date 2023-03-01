package com.api.service.unAuth.homepage.controller;

import com.api.service.config.FanyiUtil;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.entity.TSysHomePageIconEntity;
import com.api.service.db.mysql.service.ITSysHomePageIconService;
import com.api.service.jwt.JwtController;
import com.api.service.sdk.TxCosSDK.TxCosUtil;
import com.api.service.serviceReturn.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = {"主页图标"}, position = 100)
@RestController
@RequestMapping("/app/unAuth/homePage")
public class BaseuserHomePageController extends JwtController {

    @Autowired
    private ITSysHomePageIconService itSysHomePageIconService;
    @Autowired
    private TxCosUtil txCosUtil;
    @Autowired
    private FanyiUtil fanyiUtil;

    @ApiOperation(value = "查询页面图标")
    @GetMapping("/getHomePage")
    public R<List<TSysHomePageIconEntity>> getHomePage() {
        Wrapper<TSysHomePageIconEntity> wrapper = new EntityWrapper<TSysHomePageIconEntity>()
                .where("1=1")
                .orderBy("level", true);
        List<TSysHomePageIconEntity> list = itSysHomePageIconService.selectList(wrapper);
        TApiUserEntity user = getUser();
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                TSysHomePageIconEntity tSysHomePageIconEntity = list.get(i);
                tSysHomePageIconEntity.setUrl(txCosUtil.getCosObjectDownAuthUrl(tSysHomePageIconEntity.getUrl()));
                //根据用户选择的语言进行翻译
                String title = fanyiUtil.fanyi(user, tSysHomePageIconEntity.getTitle());
                tSysHomePageIconEntity.setTitle(title);
            }
        }
        return new R(0, "获取成功", list);
    }
}

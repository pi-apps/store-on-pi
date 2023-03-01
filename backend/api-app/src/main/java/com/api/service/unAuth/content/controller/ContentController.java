package com.api.service.unAuth.content.controller;


import com.api.service.config.FanyiUtil;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.entity.TSysContentEntity;
import com.api.service.db.mysql.service.ITSysContentService;
import com.api.service.serviceReturn.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.api.service.JwtThreadLocalMap.getUser;


@Api(tags = {"文章管理"}, description = "文章管理", position = 8)
@RequestMapping("/app/unAuth/content")
@RestController
public class ContentController {

    @Autowired
    private ITSysContentService itSysContentService;
    @Autowired
    private FanyiUtil fanyiUtil;

    @ApiOperation(value = "获取文章列表", notes = "获取文章列表")
    @GetMapping("/getContent")
    public R<List<TSysContentEntity>> getContent() {
        Wrapper<TSysContentEntity> wrapper = new EntityWrapper<TSysContentEntity>()
                .where("1=1")
                .eq("status", "0")
                .orderBy("create_time desc");
        List<TSysContentEntity> list = itSysContentService.selectList(wrapper);
        TApiUserEntity user = getUser();
        if (!CollectionUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                TSysContentEntity tSysContentEntity = list.get(i);
                //根据用户选择的语言进行翻译
                String name = fanyiUtil.fanyi(user, tSysContentEntity.getName());
                tSysContentEntity.setName(name);
                String content = fanyiUtil.fanyi(user, tSysContentEntity.getContext());
                tSysContentEntity.setContext(content);
            }
        }

        return new R(0, "获取成功", list);
    }


    @ApiOperation(value = "获取文章明细", notes = "1")
    @GetMapping("/getContent/{contentId}")
    public R getContentById(@ApiParam(name = "contentId", value = "主键", required = true) @PathVariable("contentId") String contentId) {

        TSysContentEntity entity = itSysContentService.selectById(contentId);
        if (null == entity) {
            return new R(1, "文章不存在", null);
        }
        if (!"0".equals(entity.getStatus())) {
            return new R(1, "未查找到文章", null);
        }
        TApiUserEntity user = getUser();
        //根据用户选择的语言进行翻译
        String name = fanyiUtil.fanyi(user, entity.getName());
        entity.setName(name);
        String content = fanyiUtil.fanyi(user, entity.getContext());
        entity.setContext(content);
        return new R(0, "获取成功", entity);
    }

}

package com.api.service.dict.controller;


import com.alibaba.fastjson.JSON;
import com.api.service.db.entity.TSysDictEntity;
import com.api.service.db.mysql.service.ITSysDictService;
import com.api.service.serviceReturn.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@Api(tags = {"公共字典"}, position = 2)
@RestController
@RequestMapping("/cert/dict")
public class CertDictController {

    @Autowired
    private ITSysDictService itSysDictService;

    @ApiOperation(value = "获取公共字典types字符串数组码值", position = 2)
    @GetMapping("/queryByType/{types}")
    public R<Map<String, List<TSysDictEntity>>> queryByTypes(@ApiParam(name = "types", value = "码值类型", required = true) @PathVariable("types") String[] types) {
        Map<String, List<TSysDictEntity>> map = itSysDictService.queryByTypes(types);
        return new R(0, "获取成功", JSON.toJSON(map));
    }

    @ApiOperation(value = "获取码值下拉列表", position = 2)
    @GetMapping("/selectByValue/{value}")
    public R<List<TSysDictEntity>> selectByValue(@ApiParam(name = "value", value = "码值类型", required = true) @PathVariable("value") String value) {
        List<TSysDictEntity> map = itSysDictService.selectByValue(value);
        return new R(0, "获取成功", JSON.toJSON(map));
    }

}

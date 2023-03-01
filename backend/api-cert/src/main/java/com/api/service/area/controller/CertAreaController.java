package com.api.service.area.controller;


import com.api.service.MapUtils;
import com.api.service.db.entity.TAreaInfoEntity;
import com.api.service.db.mysql.service.ITAreaInfoService;
import com.api.service.sdk.baiduMap.BaiduMapEntity;
import com.api.service.serviceReturn.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = {"公共地区"}, position = 2)
@RestController
@RequestMapping("/cert/area")
public class CertAreaController {

    @Autowired
    private ITAreaInfoService itAreaInfoService;

    @ApiOperation(value = "获取国家", position = 1)
    @PostMapping("/getAllCountry")
    public R<TAreaInfoEntity> getAllCountry() {
        List<TAreaInfoEntity> list = itAreaInfoService.selectByMap(new MapUtils().put("level", 0));
        return new R(0, "获取成功", list);
    }

    @ApiOperation(value = "行政区域:所有省", position = 3)
    @PostMapping("/getAllProvince")
    public R<TAreaInfoEntity> getAllProvince(@RequestBody String countryCode) {
        List<TAreaInfoEntity> list = itAreaInfoService.selectByMap(new MapUtils().put("parent_id", countryCode));

        return new R(0, "获取成功", list);
    }


    @ApiOperation(value = "行政区域:根据省份获取所有市", position = 4)
    @PostMapping("/getCityByProvince")
    public R<TAreaInfoEntity> getCityByProvince(@RequestBody String provinceCode) {
        List<TAreaInfoEntity> list = itAreaInfoService.selectByMap(new MapUtils().put("parent_id", provinceCode));

        return new R(0, "获取成功", list);
    }


    @ApiOperation(value = "行政区域:根据市获取所有区县", position = 5)
    @PostMapping("/getCountyByCity")
    public R<TAreaInfoEntity> getCountyByCity(@RequestBody String cityCode) {
        List<TAreaInfoEntity> list = itAreaInfoService.selectByMap(new MapUtils().put("parent_id", cityCode));

        return new R(0, "获取成功", list);
    }

    @ApiOperation(value = "用户当前坐标转换省 市 区/县", position = 6)
    @GetMapping("/coordinate/{lng}/{lat}")
    public R queryByType(
            @ApiParam(name = "lng", value = "经度", required = true) @PathVariable("lng") String lng,
            @ApiParam(name = "lat", value = "纬度", required = true) @PathVariable("lat") String lat

    ) {
        BaiduMapEntity baiduMapEntity = itAreaInfoService.getNameByBaidu(lng, lat);

        return new R(0, "获取成功", baiduMapEntity);
    }

}

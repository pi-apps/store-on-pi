package com.api.service.db.mysql.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.api.service.db.entity.TAreaInfoEntity;
import com.api.service.db.mysql.mapper.TAreaInfoMapper;
import com.api.service.db.mysql.service.ITAreaInfoService;
import com.api.service.sdk.baiduMap.BaiduMapEntity;
import com.api.service.sdk.baiduMap.BaiduMapSdkUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("ITMerAreaInfoService")
public class TAreaInfoServiceImpl extends ServiceImpl<TAreaInfoMapper, TAreaInfoEntity> implements ITAreaInfoService {


    @Override
    public BaiduMapEntity getNameByBaidu(String lng, String lat) {
        JSONObject jsonObject;
        try {
            //   jsonObject = BaiduMapSdkUtil.getReverseGeocoding("91.351175","29.70421");
            jsonObject = BaiduMapSdkUtil.getReverseGeocoding(lng, lat);
            if (null == jsonObject) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        String provinceName = jsonObject.getString("province");

        provinceName = provinceName.replaceAll("壮族自治区", "");
        provinceName = provinceName.replaceAll("回族自治区", "");
        provinceName = provinceName.replaceAll("维吾尔自治区", "");
        provinceName = provinceName.replaceAll("自治区", "");
        provinceName = provinceName.replaceAll("省", "");
        if ("北京市".equals(provinceName)
                || "天津市".equals(provinceName)
                || "上海市".equals(provinceName)
                || "重庆市".equals(provinceName)) {
            provinceName = provinceName.replaceAll("市", "");
        }


        String cityName = jsonObject.getString("city");
        String districtName = jsonObject.getString("district");


        Wrapper<TAreaInfoEntity> provinceWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("area_name", provinceName);
        TAreaInfoEntity province1 = this.selectOne(provinceWrapper);
        if (null == province1) {
            return null;
        }

        Wrapper<TAreaInfoEntity> regionWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("area_code", province1.getParentId());
        TAreaInfoEntity region = this.selectOne(regionWrapper);


        Wrapper<TAreaInfoEntity> cityWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("parent_id", province1.getAreaCode())
                .eq("area_name", cityName);
        TAreaInfoEntity city1 = this.selectOne(cityWrapper);
        if (null == city1) {
            return null;
        }
        Wrapper<TAreaInfoEntity> districtWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("parent_id", city1.getAreaCode())
                .eq("area_name", districtName);
        TAreaInfoEntity district1 = this.selectOne(districtWrapper);
        if (null == district1) {
            return null;
        }
        BaiduMapEntity baiduMapEntity = new BaiduMapEntity();
        baiduMapEntity.setLng(lng);
        baiduMapEntity.setLat(lat);
        baiduMapEntity.setRegionName(region.getAreaName());
        baiduMapEntity.setRegionCode(region.getAreaCode());
        baiduMapEntity.setProvinceName(province1.getAreaName());
        baiduMapEntity.setProvinceCode(province1.getAreaCode());
        baiduMapEntity.setCityName(city1.getAreaName());
        baiduMapEntity.setCityCode(city1.getAreaCode());
        baiduMapEntity.setDistrictName(district1.getAreaName());
        baiduMapEntity.setDistrictCode(district1.getAreaCode());
        baiduMapEntity.setLocationName(jsonObject.getString("street"));
        if (StringUtils.isBlank(baiduMapEntity.getLocationName())) {
            baiduMapEntity.setLocationName("街道");
        }
        return baiduMapEntity;
    }

    @Override
    public BaiduMapEntity synGetNameByBaidu(String lng, String lat) {
        JSONObject jsonObject;
        try {
            //   jsonObject = BaiduMapSdkUtil.getReverseGeocoding("91.351175","29.70421");
            jsonObject = BaiduMapSdkUtil.getReverseGeocoding(lng, lat);
            if (null == jsonObject) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        String provinceName = jsonObject.getString("province");

        provinceName = provinceName.replaceAll("壮族自治区", "");
        provinceName = provinceName.replaceAll("回族自治区", "");
        provinceName = provinceName.replaceAll("维吾尔自治区", "");
        provinceName = provinceName.replaceAll("自治区", "");
        provinceName = provinceName.replaceAll("省", "");
        if ("北京市".equals(provinceName)
                || "天津市".equals(provinceName)
                || "上海市".equals(provinceName)
                || "重庆市".equals(provinceName)) {
            provinceName = provinceName.replaceAll("市", "");
        }


        String cityName = jsonObject.getString("city");
        String districtName = jsonObject.getString("district");


        Wrapper<TAreaInfoEntity> provinceWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("area_name", provinceName);
        TAreaInfoEntity province1 = this.selectOne(provinceWrapper);
        if (null == province1) {
            return null;
        }

        Wrapper<TAreaInfoEntity> regionWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("area_code", province1.getParentId());
        TAreaInfoEntity region = this.selectOne(regionWrapper);


        Wrapper<TAreaInfoEntity> cityWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("parent_id", province1.getAreaCode())
                .eq("area_name", cityName);
        TAreaInfoEntity city1 = this.selectOne(cityWrapper);
        if (null == city1) {
            return null;
        }
        Wrapper<TAreaInfoEntity> districtWrapper = new EntityWrapper<TAreaInfoEntity>()
                .where("1=1")
                .eq("parent_id", city1.getAreaCode())
                .eq("area_name", districtName);
        TAreaInfoEntity district1 = this.selectOne(districtWrapper);

        BaiduMapEntity baiduMapEntity = new BaiduMapEntity();
        baiduMapEntity.setLng(lng);
        baiduMapEntity.setLat(lat);
        baiduMapEntity.setRegionName(region.getAreaName());
        baiduMapEntity.setRegionCode(region.getAreaCode());
        baiduMapEntity.setProvinceName(province1.getAreaName());
        baiduMapEntity.setProvinceCode(province1.getAreaCode());
        baiduMapEntity.setCityName(city1.getAreaName());
        baiduMapEntity.setCityCode(city1.getAreaCode());
        if (district1 != null) {
            baiduMapEntity.setDistrictName(district1.getAreaName());
            baiduMapEntity.setDistrictCode(district1.getAreaCode());
        }

        baiduMapEntity.setLocationName(jsonObject.getString("street"));
        if (StringUtils.isBlank(baiduMapEntity.getLocationName())) {
            baiduMapEntity.setLocationName("街道");
        }
        return baiduMapEntity;
    }
}

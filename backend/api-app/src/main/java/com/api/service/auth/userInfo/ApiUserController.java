package com.api.service.auth.userInfo;

import com.api.service.db.entity.TApiUserEntity;
import com.api.service.db.mysql.service.ITApiUserService;
import com.api.service.dto.SelectStoreByAddressEntity;
import com.api.service.jwt.JwtController;
import com.api.service.serviceReturn.R;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = {"用户管理"}, position = 100)
@RestController
@RequestMapping("/app/auth/userInfo")
public class ApiUserController extends JwtController {


    @Autowired
    private ITApiUserService itApiUserService;

    @ApiOperation(value = "查询本人用户信息", position = 1)
    @PostMapping("/info")
    public R<TApiUserEntity> info() {
        try {
            String userId = getUserId();
            TApiUserEntity tApiUserEntity = itApiUserService.selectById(userId);
            return new R(0, "成功", tApiUserEntity);
        } catch (Exception e) {
            return new R(1, fail(e), null);
        }
    }

    @ApiOperation(value = "用户绑定上级(邀请码就是PI用户名)", position = 2)
    @PostMapping("/bindingParentId")
    public R<TApiUserEntity> bindingParentId(@RequestBody String invitation) {
        try {
            String userId = getUserId();
            TApiUserEntity tApiUserEntity = itApiUserService.selectById(userId);
            String recommenderUserId = tApiUserEntity.getRecommenderUserId();
            if (StringUtils.isNotBlank(recommenderUserId)) {
                return new R(1, "已绑定过上级,不支持更改", null);
            }
            Wrapper<TApiUserEntity> wrapper = new EntityWrapper<TApiUserEntity>()
                    .where("1=1")
                    .eq("invitation", invitation);
            TApiUserEntity tApiUserEntity1 = itApiUserService.selectOne(wrapper);
            if (null == tApiUserEntity1) {
                return new R(1, "上级未在本平台注册", null);
            }
            if (tApiUserEntity1.getUserId().equals(userId)) {
                return new R(1, "不能绑定自己", null);
            }
            tApiUserEntity.setRecommenderUserId(tApiUserEntity1.getUserId());
            itApiUserService.updateById(tApiUserEntity);
            return new R(0, "绑定成功", null);

        } catch (Exception e) {
            return new R(1, fail(e), null);
        }
    }

    @ApiOperation(value = "绑定常驻地址,每次选择区域都更新此值,用于记录用户所在城市", position = 2)
    @PostMapping("/bindingLocation")
    public R<TApiUserEntity> bindingLocation(@RequestBody SelectStoreByAddressEntity addressEntity) {
        try {
            String userId = getUserId();
            TApiUserEntity tApiUserEntity = itApiUserService.selectById(userId);
            tApiUserEntity.setCityCode(addressEntity.getCityCode());
            tApiUserEntity.setDistrictCode(addressEntity.getDistrictCode());
            tApiUserEntity.setProvinceCode(addressEntity.getProvinceCode());
            tApiUserEntity.setCountryCode(addressEntity.getCountryCode());
            itApiUserService.updateById(tApiUserEntity);
            return new R(0, "绑定成功", null);
        } catch (Exception e) {
            return new R(1, fail(e), null);
        }
    }

    @ApiOperation(value = "绑定用户语言", position = 3)
    @PostMapping("/bindingLanguage")
    public R<TApiUserEntity> bindingLanguage(@RequestBody String language) {
        try {
            String userId = getUserId();
            TApiUserEntity tApiUserEntity = itApiUserService.selectById(userId);
            tApiUserEntity.setLanguage(language);
            itApiUserService.updateById(tApiUserEntity);
            return new R(0, "绑定成功", null);
        } catch (Exception e) {
            return new R(1, fail(e), null);
        }
    }
}

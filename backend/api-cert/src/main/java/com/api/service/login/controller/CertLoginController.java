package com.api.service.login.controller;

import com.api.service.MapUtils;
import com.api.service.ShiroThreadLocalMap;
import com.api.service.UUID;
import com.api.service.db.entity.TCmsUserEntity;
import com.api.service.db.mysql.service.ITCMSUserService;
import com.api.service.db.mysql.service.ITCMSUserTokenService;
import com.api.service.login.entity.CmsLoginEntity;
import com.api.service.login.service.ICertService;
import com.api.service.sdk.TxCosSDK.TxCosUtil;
import com.api.service.serviceReturn.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = {"统一登录"}, description = "统一登录", position = 2)
@RequestMapping("/cert/access")
@RestController
public class CertLoginController {

    @Autowired
    private ICertService iCertService;

    @Autowired
    private ITCMSUserService itcmsUserService;

    @Autowired
    private ITCMSUserTokenService itcmsUserTokenService;

    @Autowired
    private TxCosUtil txCosUtil;


    @ApiOperation(value = "CMS登录接口", position = 1, notes = "登录接口")
    @PostMapping("/cmsIn")
    public R cmsIn(@RequestBody CmsLoginEntity form) {
        //根据手机号码获取用户信息
        TCmsUserEntity userEntity = itcmsUserService.selectByMobile(form.getMobile());
        if (null == userEntity) {
            return new R(1, "用户名或密码不正确[N]", null);
        }
        if (!userEntity.getPassword().equals(new Sha256Hash(form.getPassword(), userEntity.getSalt()).toHex())) {
            return new R(2, "用户名或密码不正确[P]", null);
        }
        String token = itcmsUserTokenService.createToken(userEntity.getUserId(), UUID.randomUUID());
        if (StringUtils.isBlank(token)) {
            return new R(1, "鉴权令牌获取失败", null);
        }
        ShiroThreadLocalMap.setUserId(userEntity.getUserId());//将user装入当前线程内
        ShiroThreadLocalMap.setToken(token);//将token装入当前线程内
        ShiroThreadLocalMap.setMobile(userEntity.getMobile());
        Subject subject = SecurityUtils.getSubject();
        //将token交给shiro认证
        subject.login(new UsernamePasswordToken(userEntity.getUserId(), token));
        if (StringUtils.isNotBlank(userEntity.getUserHeadUrl())) {
            userEntity.setUserHeadUrl(txCosUtil.getCosObjectDownAuthUrl(userEntity.getUserHeadUrl()));
        }
        return new R(0, "请求成功", new MapUtils().put("token", token).put("expire", 3600 * 24 * 15L).put("user", userEntity));

    }
}

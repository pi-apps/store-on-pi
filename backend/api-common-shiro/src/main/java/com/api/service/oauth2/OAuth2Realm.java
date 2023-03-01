package com.api.service.oauth2;

import com.api.service.ShiroThreadLocalMap;
import com.api.service.db.entity.TCmsUserEntity;
import com.api.service.db.entity.TCmsUserTokenEntity;
import com.api.service.db.mysql.service.ITCMSUserService;
import com.api.service.db.mysql.service.ITCMSUserTokenService;
import com.api.service.shiroEntity.ShiroUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证
 */
@Component("oAuth2Realm")
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    private ITCMSUserService itcmsUserService;
    @Autowired
    private ITCMSUserTokenService itcmsUserTokenService;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUserEntity shiroUserEntity = (ShiroUserEntity) principals.getPrimaryPrincipal();
        this.verified(shiroUserEntity);
        Set<String> permsSet = shiroUserEntity.getPermsSet();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String userId = String.valueOf(usernamePasswordToken.getUsername());//获取用户手机号码
        String userToken = String.valueOf(usernamePasswordToken.getPassword());//获取用户密码

        TCmsUserTokenEntity tUserTokenEntity = itcmsUserTokenService.selectById(userId);
        if (null == tUserTokenEntity) {
            throw new RuntimeException("CMS:令牌获取失败,请重新登录");
        }
        if (!userToken.equals(tUserTokenEntity.getToken())) {
            throw new RuntimeException("CMS:令牌已发生变动,请重新登录");
        }
        TCmsUserEntity tUserEntity = itcmsUserService.selectById(userId);
        if (null == tUserEntity) {
            throw new RuntimeException("CMS:用户数据已不存在,请重新登录");
        }
        if (!"0".equals(tUserEntity.getStatus())) {
            throw new RuntimeException("CMS:账号已被限制,请联系管理员");
        }
        //校验通过后
        ShiroThreadLocalMap.setMobile(tUserEntity.getMobile());
        ShiroThreadLocalMap.setUserId(tUserEntity.getUserId());
        ShiroThreadLocalMap.setToken(tUserTokenEntity.getToken());
        //封装权限实体
        ShiroUserEntity shiroUserEntity = new ShiroUserEntity();
        shiroUserEntity.setUserId(tUserEntity.getUserId());
        shiroUserEntity.setMobile(tUserEntity.getMobile());
        shiroUserEntity.setStatus(tUserEntity.getStatus());
        shiroUserEntity.setToken(tUserTokenEntity.getToken());
        shiroUserEntity.setPermsSet(itcmsUserService.queryAllPerms(userId));
        shiroUserEntity.setExpireTime(tUserTokenEntity.getExpireTime());
        shiroUserEntity.setT(tUserEntity);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(shiroUserEntity, shiroUserEntity.getToken(), getName());
        return info;
    }


    private void verified(ShiroUserEntity shiroUserEntity) {
        if (null == shiroUserEntity) {
            throw new LockedAccountException("CMS:当前用户权限过期,请重新登录");
        }
        if (!"0".equals(shiroUserEntity.getStatus())) {
            try {
                SecurityUtils.getSubject().logout();
            } catch (Exception e) {

            }
            throw new LockedAccountException("CMS:账号已禁用！申诉请联系管理员。");
        }
        if (!shiroUserEntity.getUserId().equals(ShiroThreadLocalMap.getUserId())) {
            try {
                SecurityUtils.getSubject().logout();
            } catch (Exception e) {

            }
        }
        if (shiroUserEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
            try {
                SecurityUtils.getSubject().logout();
            } catch (Exception e) {

            }
            throw new IncorrectCredentialsException("CMS:token失效，请重新登录");
        }
        if (!shiroUserEntity.getToken().equals(ShiroThreadLocalMap.getToken())) {
            try {
                SecurityUtils.getSubject().logout();
            } catch (Exception e) {

            }
            throw new IncorrectCredentialsException("CMS:token失效，请重新登录");
        }

    }


}

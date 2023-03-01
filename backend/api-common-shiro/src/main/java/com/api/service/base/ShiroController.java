package com.api.service.base;

import com.api.service.ShiroThreadLocalMap;
import com.api.service.db.entity.TCmsUserEntity;
import com.api.service.shiroEntity.ShiroUserEntity;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

/**
 * Shiro工具类
 */
public class ShiroController {


    private ShiroUserEntity<TCmsUserEntity> get() {
        SimplePrincipalCollection simplePrincipalCollection =
                (SimplePrincipalCollection) ShiroThreadLocalMap.getShiroTokenSessionEntity().getSession().getAttribute
                        (DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        return (ShiroUserEntity<TCmsUserEntity>) simplePrincipalCollection.getPrimaryPrincipal();

    }

    public TCmsUserEntity getUser() {

        return this.get().getT();
    }

    public String getUserId() {
        return getUser().getUserId();
    }

    public String success() {
        return "处理成功";
    }

    public String fail(Exception e) {
        if (e instanceof RuntimeException) {
            RuntimeException exception = (RuntimeException) e;
            return "处理失败:" + exception.getMessage();
        }
        return "处理失败:" + e.getMessage();
    }

}

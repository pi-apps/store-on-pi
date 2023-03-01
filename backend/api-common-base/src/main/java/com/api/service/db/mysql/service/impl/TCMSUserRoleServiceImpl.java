package com.api.service.db.mysql.service.impl;

import com.api.service.MapUtils;
import com.api.service.db.mysql.BaseMysqlTable;
import com.api.service.db.mysql.mapper.TCMSUserRoleMapper;
import com.api.service.db.mysql.service.ITCMSRoleMenuService;
import com.api.service.db.mysql.service.ITCMSRoleService;
import com.api.service.db.mysql.service.ITCMSUserRoleService;
import com.api.service.db.mysql.sql.service.ISysSqlService;
import com.api.service.db.mysql.sql.util.SqlBaseUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.api.service.UUID;;

/**
 * @author Ashy.Cheung
 * @date 2020.07.20
 */
@Service("ITCMSUserRoleService")
public class TCMSUserRoleServiceImpl extends ServiceImpl<TCMSUserRoleMapper, TCmsUserRoleEntity> implements ITCMSUserRoleService {

    @Autowired
    private ITCMSRoleService itRoleService;
    @Autowired
    private ITCMSRoleMenuService itRoleMenuService;
    @Autowired
    private ISysSqlService iSysSqlService;

    @Override
    public List<TCmsRoleEntity> queryAllRole(String userId) {
        Wrapper<TCmsUserRoleEntity> wrapper = new EntityWrapper<TCmsUserRoleEntity>()
                .where("1=1")
                .eq("user_id", userId)
                .setSqlSelect("role_id");
        List<Object> list = this.selectObjs(wrapper);
        List<String> strs;
        if (null != list && 0 < list.size()) {
            strs = new ArrayList<>();
            for (Object o : list) {
                strs.add((String) o);
            }
            List<TCmsRoleEntity> userRoles = itRoleService.selectBatchIds(strs);
            for (TCmsRoleEntity t : userRoles) {
                t.setMenus(itRoleMenuService.queryMenuList(t.getRoleId()));
            }
            return userRoles;
        }
        return null;
    }

    @Override
    public int deleteBatch(String[] roleIds) {
        for (String roleId : roleIds) {
            super.baseMapper.deleteByMap(new MapUtils().put("role_id", roleId));
        }
        return 0;
    }

    @Override
    public List<String> queryRoleIdList(String userId) {
        String sql = "select role_id AS var1 from " + BaseMysqlTable.USER_ROLE_TABLE + " where user_id = '" + userId + "'";
        List<Map<String, Object>> listMap = iSysSqlService.selectSql(sql);
        return SqlBaseUtil.SqlReturnListObj(listMap, null);
    }

    @Override
    public void saveOrUpdate(String userId, List<String> roleIdList) {
        //先删除用户与角色关系
        this.deleteByMap(new MapUtils().put("user_id", userId));
        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }
        //保存用户与角色关系
        List<TCmsUserRoleEntity> list = new ArrayList<>(roleIdList.size());
        for (String roleId : roleIdList) {
            TCmsUserRoleEntity tIpmsUserRoleEntity = new TCmsUserRoleEntity();
            tIpmsUserRoleEntity.setId(UUID.randomUUID());
            tIpmsUserRoleEntity.setUserId(userId);
            tIpmsUserRoleEntity.setRoleId(roleId);
            list.add(tIpmsUserRoleEntity);
        }
        this.insertBatch(list);
    }
}

package com.api.service.db.mysql.mapper;

import com.api.service.db.mysql.BaseMysqlTable;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface TCMSUserMapper extends BaseMapper<TCmsUserEntity> {

    /**
     * 查询用户所有权限标签-->用户--->多个角色--->多个菜单去重复
     *
     * @param user_id
     * @return
     */
    @Select("SELECT " +
            "m.perms AS perms " +
            "FROM " + BaseMysqlTable.USER_ROLE_TABLE + " ur " +
            "LEFT JOIN " + BaseMysqlTable.ROLE_MENU_TABLE + " rm ON ur.role_id = rm.role_id " +
            "LEFT JOIN " + BaseMysqlTable.MENU_TABLE + " m ON rm.menu_id = m.menu_id " +
            "WHERE ur.user_id = #{user_id} " +
            "AND m.perms is not null " +
            "AND  m.perms<>''")
    List<String> queryAllPerms(@Param("user_id") String user_id);

    /**
     * 获取用户所有菜单及接口主键
     *
     * @param user_id
     * @return
     */
    @Select("SELECT DISTINCT " +
            "rm.menu_id AS menu_id " +
            "FROM " + BaseMysqlTable.USER_ROLE_TABLE + " ur " +
            "LEFT JOIN " + BaseMysqlTable.ROLE_MENU_TABLE + " rm ON ur.role_id = rm.role_id " +
            "WHERE ur.user_id = #{user_id} " +
            "AND rm.menu_id IS NOT NULL " +
            "AND rm.menu_id <> ''")
    List<String> queryAllMenuId(@Param("user_id") String user_id);

}

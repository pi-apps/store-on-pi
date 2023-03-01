package com.api.service.db.mysql;


/**
 * 权限基础表命名
 */
public class BaseMysqlTable {

    /**
     * cms相关
     */
    public static final String USER_TABLE = "t_cms_user_info"; //cms用户主表

    public static final String USER_TOEKN_TABLE = "t_cms_user_token";//cms用户令牌表

    public static final String USER_ROLE_TABLE = "t_cms_user_role";//cms用户角色表

    public static final String ROLE_TABLE = "t_cms_role";//cms角色表

    public static final String MENU_TABLE = "t_cms_menu";//cms菜单表

    public static final String ROLE_MENU_TABLE = "t_cms_role_menu";//cms角色菜单表

    /**
     * api相关
     */
    public static final String API_USER_TABLE = "t_api_user_info"; //主用户主表


}

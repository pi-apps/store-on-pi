package com.api.service.db.mysql.sql.service;

import java.util.List;
import java.util.Map;

/**
 * 机构管理
 */
public interface ISysSqlService {

	List<Map<String,Object>> selectSql(String sql);


}

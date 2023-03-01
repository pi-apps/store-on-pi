package com.api.service.db.mysql.sql.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysSqlDao {

    List<Map<String, Object>> selectSql(@Param("mapper/sql") String sql);

}

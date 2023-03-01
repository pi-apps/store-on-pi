package com.api.service.db.mysql.sql.service.impl;

import com.api.service.db.mysql.sql.dao.SysSqlDao;
import com.api.service.db.mysql.sql.service.ISysSqlService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("ISysSqlService")
public class SysSqlServiceImpl implements ISysSqlService {
    @Autowired
    private SysSqlDao sysSqlDao;

    @Override
    public List<Map<String, Object>> selectSql(String sql) {
        if (StringUtils.isBlank(sql)) {
            throw new RuntimeException("sql语句为空");
        }
        if (sql.indexOf("insert ") != -1 || sql.indexOf("update ") != -1 || sql.indexOf("delete ") != -1) {
            throw new RuntimeException("sql语句不能含有inser,update,delect关键字操作");
        }
        return sysSqlDao.selectSql(sql);
    }
}

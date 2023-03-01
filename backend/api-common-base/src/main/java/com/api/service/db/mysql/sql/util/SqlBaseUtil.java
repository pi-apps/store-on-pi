package com.api.service.db.mysql.sql.util;


import com.api.service.db.mysql.util.beanToMap.BeanMapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于注入sql解析
 *
 * @author Ashy.Cheung
 */
public class SqlBaseUtil {

    public static List SqlReturnListObj(List<Map<String, Object>> listMap, Class type) {
        List list = null;
        if (null != listMap) {
            list = new ArrayList<>();
            for (Map<String, Object> map : listMap) {
                if (null != map) {
                    if (null != type) {
                        list.add(BeanMapUtil.convertMap(type, map));
                    } else {
                        list.add(String.valueOf(map.get("var1")));
                    }

                }
            }
        }
        return list;
    }

    public static Object SqlReturnOne(List<Map<String, Object>> listMap, Class type) {
        if (null != listMap && listMap.size() > 0) {
            return BeanMapUtil.convertMap(type, listMap.get(0));
        }
        return null;
    }
}

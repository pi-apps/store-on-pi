package com.api.service.db.mysql.util;

import org.apache.commons.lang.StringUtils;

import static cn.hutool.core.util.CharUtil.UNDERLINE;

public class CamelUnderlineUtil {

    /**
     * 驼峰命名转换成下划线方式名称，eg：cfConfigRecord > cf_config_record
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int len = param.length();
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线方式名称转换成驼峰命名，eg：cf_config_record > cfConfigRecord
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param){
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int len = param.length();
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c==UNDERLINE) {
                if(++i<len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

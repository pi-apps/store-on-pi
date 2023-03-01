package com.api.service.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.service.RedisUtil;
import com.api.service.db.entity.TApiUserEntity;
import com.api.service.sdk.kdn.KdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class FanyiUtil {
    @Autowired
    private RedisUtil redisUtil;

    public String fanyi(TApiUserEntity user, String content) {
        if (null != user && !StringUtils.isEmpty(content)) {
            String language = user.getLanguage();
            if (!StringUtils.isEmpty(language)) {
                //获取redis，如果redis没有调用第三方进行翻译
                String result = (String) redisUtil.redisTemplate().opsForHash().get(content, language);
                if (StringUtils.isEmpty(result)) {
                    Map<String, String> params = new HashMap<>();
                    params.put("from", "chinese_simplified");
                    params.put("to", language);
                    params.put("text", "[\"" + content + "\"]");
                    try {
                        result = KdUtil.sendPost("http://api.translate.zvo.cn/translate.json", params);
                        JSONObject jsonObject1 = JSONObject.parseObject(result);
                        String result1 = jsonObject1.getString("result");
                        if ("1".equals(result1)) {
                            JSONArray array = jsonObject1.getJSONArray("text");
                            result = array.getString(0);
                            redisUtil.redisTemplate().opsForHash().put(content, language, result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //如果最终没翻译成功，返回原报文
                if (StringUtils.isEmpty(result)) {
                    return content;
                }
                return result;
            }
        }
        return content;
    }
}

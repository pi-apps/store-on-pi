package com.api.service.gateway;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于微服务调度失败后统一响应
 *
 * @author Ashy.cheung
 * @date 2020.12.03
 */

@RestController
public class BaseFallBack {

    @RequestMapping("/sys/fallback")
    @ResponseBody
    public String fallback(String service) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 999);
        jsonObject.put("msg", "服务器[" + service + "]临时维护,请稍后重试");
        return jsonObject.toString();
    }
}

package com.api.service.feginclients.baseuserService;

import com.api.service.feginclients.baseuserService.fallBack.FeginsBaseuserUserControllerCallFallBack;
import com.api.service.serviceReturn.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: knight
 * Date: 2020/7/23
 * Time: 16:48
 * Description:
 */
@FeignClient(value = "baseuser-service", path = "/fegins/baseuser/user", fallback = FeginsBaseuserUserControllerCallFallBack.class)
public interface FeginsBaseuserUserControllerCall {
    @PostMapping("/test")
    R test(@RequestBody Map<String, String> map);
}

package com.api.service.feginclients.baseuserService.fallBack;

import com.api.service.feginclients.baseuserService.FeginsBaseuserUserControllerCall;
import com.api.service.serviceReturn.R;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FeginsBaseuserUserControllerCallFallBack implements FeginsBaseuserUserControllerCall {


    @Override
    public R test(Map<String, String> map) {
        return null;
    }
}

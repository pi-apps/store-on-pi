
package com.api.service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 服务器端key
 */
@Configuration
@Data
public class CommonConfig {

    @Value("${sdk.serverAccessKey}")
    private String serverAccessKey;
}

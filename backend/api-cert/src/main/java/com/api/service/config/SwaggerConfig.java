package com.api.service.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig implements WebMvcConfigurer {
    @Value("${swagger.enabled}")
    private boolean swaggerEnabled;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
                .select()
                //加了ApiOperation注解的类，才生成接口文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //包下的类，才生成接口文档
//                .apis(RequestHandlerSelectors.basePackage("com.tuyerepig.platform.modules.*.service"))
                .paths(PathSelectors.any())
                .build();
                //.securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("微服务-认证服务系统API接口文档")
                .description("Ashy框架-接口文档")
                .termsOfServiceUrl("http://www.ashy.com")
                .version("1.0.0")
                .build();
    }

//    private List<ApiKey> security() {
//        return newArrayList(
//                new ApiKey("token", "token", "header"),
//                new ApiKey("user_id", "user_id", "header")
//        );
//    }

}
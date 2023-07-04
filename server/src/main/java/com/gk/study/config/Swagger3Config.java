package com.gk.study.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@EnableKnife4j
@Configuration
@EnableOpenApi
public class Swagger3Config {

    @Bean
    public Docket coreApiConfig(){
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getApiInfo())
                .groupName("apis")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gk.study.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("api文档")
                .description("管理系统接口描述")
                .version("1.0")
                .build();
    }
}

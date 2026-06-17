package com.example.finance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j / OpenAPI 文档配置。
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI financeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("个人财务管理系统接口文档")
                        .description("基于 Spring Boot 3、MyBatis-Plus、MySQL 的个人财务管理系统")
                        .version("1.0.0")
                        .contact(new Contact().name("finance-system")));
    }
}

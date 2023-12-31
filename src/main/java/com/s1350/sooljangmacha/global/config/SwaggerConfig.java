package com.s1350.sooljangmacha.global.config;

// Java

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("술병마차 API")
                .description("술병마차 API 명세서")
                .version("1.0.0");

        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer", bearerAuth))
                .addSecurityItem(addSecurityItem)
                .info(info);
    }



    private Info apiInfo() {
        return new Info()
                .title("술병마차 API")
                .description("술병마차 API 명세서")
                .version("1.0.0");
    }
}

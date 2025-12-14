package org.web.hikarihotelmanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.domain.name:}")
    private String ngrokUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();
        
        if (ngrokUrl != null && !ngrokUrl.isEmpty()) {
            servers.add(new Server()
                    .url(ngrokUrl));
        }

        servers.add(new Server()
                .url("http://localhost:8080"));

        return new OpenAPI()
                .info(new Info()
                        .title("Hikari Hotel Management API")
                        .version("1.0")
                        .description("API Documentation for Hikari Hotel Management System")
                        .contact(new Contact()
                                .name("Hikari Hotel Team")
                                .email("hikari.hotel.contact@gmail.com")))
                .servers(servers)
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Nhập JWT token để xác thực")));
    }
}

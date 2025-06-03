package com.example.UserServiceApplication.ProductMicroservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Product Service Documentation")
                        .version("1.0")
                        .description("API documentation for the Product Service"))
                .addServersItem(new Server()
                        .url("http://localhost:8085")
                        .description("Local Development Server"))
                .addServersItem(new Server()
                        .url("https://api.example.com")
                        .description("Production Server"))
                .addServersItem(new Server()
                        .url("https://staging.example.com")
                        .description("Staging Server"));
    }
}
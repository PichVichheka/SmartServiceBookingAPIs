package com.smartService.SmartServiceBookingAPIs.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartService Booking APIs")
                        .version("1.0")
                        .description("Swagger UI for SmartService Booking Application"))
                .addServersItem(new Server().url("http://localhost:8081")); // server port
    }
}

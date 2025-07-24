package com.sasf.ktestfull.Config.Swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Prueba ktestfull")
                        .version("1.0")
                        .description("Documentación interactiva de prueba ktestfull")
                        .contact(new Contact()
                                .name("Kelvin Vargas")
                                .email("kelvin.vargas@sasf.net")));
    }
}

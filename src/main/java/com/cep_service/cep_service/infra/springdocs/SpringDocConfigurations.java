package com.cep_service.cep_service.infra.springdocs;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cep-Service")
                        .version("1.0.0")
                        .description("API de informações de CEPs. \n\n 🚀 Versão com Deploy Automático via GitHub Actions!")
                        .contact(new Contact()
                                .name("Gabriel")
                                .email("oliveirafrerreira97@hotmail.com")

                        )

                ) .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
package com.threeriversbank.banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.server-url}")
    private String openApiServerUrl;

    @Bean
    public OpenAPI bankingOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl(openApiServerUrl);
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Three Rivers Bank");
        contact.setEmail("support@threeriversbank.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Three Rivers Bank API")
                .version("1.0.0")
                .description("REST API for Three Rivers Bank online banking system. " +
                        "This API provides endpoints for account management, transaction history, and fund transfers.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}

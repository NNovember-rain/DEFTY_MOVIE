package com.defty.movie.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "API document",
                version = "1.0.0",
                description = "Description of API document"
        ),
        servers = {
                @Server(url = "http://localhost:8088", description = "Local Development Server")
        }
)

@Configuration
public class OpenApiConfig {

}

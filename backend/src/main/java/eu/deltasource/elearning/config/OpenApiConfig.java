package eu.deltasource.elearning.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuration class for customizing the OpenAPI documentation for the online learning platform.
 * This class sets up the OpenAPI specification with custom information about the API.
 */
@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online learning platform")
                        .description("Watch videos on different IT related topics")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .servers(Arrays.asList(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Main API server"),
                        new Server()
                                .url("http://localhost:8080/actuator")
                                .description("Actuator endpoints")
                ));
    }
}

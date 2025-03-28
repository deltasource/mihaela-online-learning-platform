package eu.deltasource.demo.config;

import eu.deltasource.demo.model.Instructor;
import eu.deltasource.demo.repository.InstructorRepository;
import eu.deltasource.demo.service.InstructorService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * Configuration class for customizing the OpenAPI documentation for the online learning platform.
 * This class sets up the OpenAPI specification with custom information about the API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online learning platform")
                        .description("Watch videos on different IT related topics")
                        .version("1.0.0"));
    }
}

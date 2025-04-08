package eu.deltasource.elearning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig is a configuration class that customizes the Spring MVC settings.
 * It implements the WebMvcConfigurer interface to provide custom configurations
 * for serving static resources, specifically video files in this case.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * The directory path where video files are uploaded.
     * This value is injected from the application's properties file.
     */
    @Value("${video.upload.directory}")
    private String videoUploadDirectory;

    /**
     * Configures resource handlers to serve static resources.
     * This implementation adds a handler for URLs matching "/videos/**",
     * mapping them to the physical location specified by videoUploadDirectory.
     *
     * @param registry the ResourceHandlerRegistry to add resource handlers to
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("file:" + videoUploadDirectory + "/");
    }
}

package example.chatBot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// This class configures Cross-Origin Resource Sharing (CORS) for your Spring Boot application.
// CORS is needed to allow your Android app (or any client from a different origin)
// to make requests to your backend API.
@Configuration
public class CorsConfig {

    // This is one common way to configure CORS using a CorsFilter bean.
    // It's generally more flexible than using WebMvcConfigurer for complex scenarios.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials (like cookies, authorization headers)
        config.setAllowCredentials(true);

        // Allow requests from any origin.
        // In a production environment, you should restrict this to specific origins
        // like your Android app's expected origin, if applicable (e.g., "http://localhost:8080"
        // if running on an emulator, or your production domain).
        // For development and testing, allowing all origins is often convenient.
        config.addAllowedOriginPattern("*"); // Use addAllowedOriginPattern for '*' with credentials

        // Allow all common HTTP methods (GET, POST, PUT, DELETE, OPTIONS, etc.)
        config.addAllowedMethod("*");

        // Allow all common headers
        config.addAllowedHeader("*");

        // Apply this CORS configuration to all paths in your application.
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    // Another way to configure CORS is by implementing WebMvcConfigurer.
    // This is often simpler for basic CORS needs.
    // You can use either the CorsFilter bean above OR this WebMvcConfigurer bean,
    // but typically not both for the same paths, to avoid conflicting configurations.
    // The CorsFilter approach is shown as the primary method above.
    /*
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all paths
                        .allowedOrigins("*") // Allow from any origin (restrict in production)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
                        .allowedHeaders("*") // Allowed headers
                        .allowCredentials(true); // Allow credentials
            }
        };
    }
    */
}


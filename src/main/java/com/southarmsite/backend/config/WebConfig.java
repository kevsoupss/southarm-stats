package com.southarmsite.backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // allow CORS on all endpoints
                        .allowedOrigins("http://localhost:8000")  // allow your React app's origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // allow HTTP methods
                        .allowedHeaders("*");
            }
        };
    }
}

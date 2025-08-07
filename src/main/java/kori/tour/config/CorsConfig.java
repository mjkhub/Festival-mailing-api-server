package kori.tour.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class CorsConfig {  // 스프링 시큐리티로 처리할지는 조금 더 고민해보자

//    @Value("${service-url}")
    private String serviceURL;

    @Bean
    public WebMvcConfigurer corsConfigurer() { //개발 cors
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(serviceURL)
                        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH", "HEAD")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }

}

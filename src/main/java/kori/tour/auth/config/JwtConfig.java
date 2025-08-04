package kori.tour.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import kori.tour.auth.token.JwtTokenProperties;

@Configuration
@EnableConfigurationProperties(JwtTokenProperties.class)
public class JwtConfig {

}

package kori.tour.auth.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtTokenProperties (
        String secret,
        long expirationTime){
}

package kori.tour.auth.token;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import kori.tour.auth.exception.UnauthorizedException;

@Component
public class JwtTokenProvider {

    private final JwtTokenProperties jwtTokenProperties;

    private final Key key;

    /**
     * Constructs a JwtTokenProvider with the specified JWT configuration properties.
     *
     * Initializes the cryptographic signing key using the Base64-decoded secret from the provided properties.
     */
    public JwtTokenProvider(JwtTokenProperties jwtTokenProperties) {
        this.jwtTokenProperties = jwtTokenProperties;
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtTokenProperties.secret()));
    }

    /**
     * Generates a JWT access token for the specified principal.
     *
     * The token includes the principal as the subject, the current time as the issued-at timestamp, and an expiration time based on the configured duration. The token is signed using the HS256 algorithm.
     *
     * @param principal the identifier to set as the subject of the token
     * @return a signed JWT access token as a compact string
     */
    public String createAccessToken(String principal) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // 헤더의 타입(typ)을 "JWT"로 설정 -> alg는 서명 시 자동으로 설정
                .setSubject(principal)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtTokenProperties.expirationTime()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the principal (subject) from a JWT access token.
     *
     * Parses the provided JWT token using the configured signing key and returns the subject claim.
     * Throws an {@code UnauthorizedException} if the token is invalid or cannot be parsed.
     *
     * @param token the JWT access token to parse
     * @return the principal (subject) extracted from the token
     */
    public String extractPrincipal(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new UnauthorizedException(e);
        }
    }



}

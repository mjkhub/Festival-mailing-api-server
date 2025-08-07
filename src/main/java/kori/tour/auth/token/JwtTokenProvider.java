package kori.tour.auth.token;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import kori.tour.auth.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	private final JwtTokenProperties jwtTokenProperties;

	private final Key key;

	public JwtTokenProvider(JwtTokenProperties jwtTokenProperties) {
		this.jwtTokenProperties = jwtTokenProperties;
		this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtTokenProperties.secret()));
	}

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

	public String extractPrincipal(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
		}
		catch (ExpiredJwtException e) {
			log.warn("JWT token expired: {}", e.getMessage());
			throw new UnauthorizedException(e);
		}
		catch (MalformedJwtException e) {
			log.warn("Malformed JWT token: {}", e.getMessage());
			throw new UnauthorizedException(e);
		}
		catch (JwtException e) {
			log.error("JWT validation failed: {}", e.getMessage());
			throw new UnauthorizedException(e);
		}
	}

}

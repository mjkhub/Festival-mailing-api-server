package kori.tour.auth.token;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kori.tour.auth.exception.UnauthorizedException;

class JwtTokenProviderTest {

	// this is test key
	private String secretKey = "UEqyvLxxgSMean86gG4QLbF1wtdKw9bSYgq8FRhmPXU=";

	private long expireTime = 3600_000;

	@Test
	@DisplayName("토큰을 성공적으로 발급하고 payload 추출한다")
	void parse_payload_by_valid_token() {
		// given
		JwtTokenProperties jwtTokenProperties = new JwtTokenProperties(secretKey, expireTime);
		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtTokenProperties);
		String memberId = "1";

		// when
		String accessToken = jwtTokenProvider.createAccessToken(memberId);

		// then
		assertThat(jwtTokenProvider.extractPrincipal(accessToken)).isEqualTo(memberId);

	}

	@Test
	@DisplayName("만료된 토큰에서 payload 추출 시 예외를 반환한다")
	void parse_payload_by_expired_token() {
		// given
		JwtTokenProperties properties = new JwtTokenProperties(secretKey, -1);
		JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(properties);
		String memberId = "1";
		String accessToken = jwtTokenProvider.createAccessToken(memberId);

		// when
		ThrowableAssert.ThrowingCallable callable = () -> jwtTokenProvider.extractPrincipal(accessToken);

		// then
		assertThatThrownBy(callable).isInstanceOf(UnauthorizedException.class);
	}

	@Test
	@DisplayName("잘못된 키로 토큰을 발급할 경우 payload 추출 시 예외를 반환한다")
	void parse_payload_by_invalid_token() {
		// given
		JwtTokenProperties validProperties = new JwtTokenProperties(secretKey, expireTime);
		JwtTokenProperties invalidProperties = new JwtTokenProperties("vKZuLthEBiXJlWmeaUFWsyvRlxxW7aNCOHA8PQFFyJQ=",
				expireTime);

		JwtTokenProvider validJwtTokenProvider = new JwtTokenProvider(validProperties);
		JwtTokenProvider invalidJwtTokenProvider = new JwtTokenProvider(invalidProperties);

		String memberId = "1";
		String accessToken = validJwtTokenProvider.createAccessToken(memberId);

		// when
		ThrowableAssert.ThrowingCallable callable = () -> invalidJwtTokenProvider.extractPrincipal(accessToken);

		// then
		assertThatThrownBy(callable).isInstanceOf(UnauthorizedException.class);
	}

}

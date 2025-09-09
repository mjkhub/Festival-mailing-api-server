package kori.tour.auth.token;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String NO_CHECK_URL = "/api/login/oauth";
    private static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith(BEARER))
                .map(h -> h.substring(BEARER.length()))
                .orElseGet(()->"");

        // AnonymousAuthenticationFilter 가 실행 -> .anyRequest().authenticated())과 만나 403이 반환
        if (request.getRequestURI().startsWith(NO_CHECK_URL) || accessToken.isEmpty()) { //
            filterChain.doFilter(request, response);
            return;
        }

        String memberId = jwtTokenProvider.extractPrincipal(accessToken);

        MemberPrincipal principal = new MemberPrincipal(memberId);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                java.util.Collections.emptyList() // 권한 없음
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}

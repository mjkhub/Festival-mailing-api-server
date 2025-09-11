package kori.tour.global.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import kori.tour.auth.exception.UnauthorizedException;
import kori.tour.auth.token.MemberPrincipal;

public class SecurityUtils {

    public static Long getCurrentMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof MemberPrincipal p)) {
            throw new UnauthorizedException(null);
        }
        return p.memberId() == null ? null : Long.parseLong(p.memberId());
    }
}

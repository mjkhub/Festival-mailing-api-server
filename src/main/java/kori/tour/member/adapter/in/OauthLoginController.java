package kori.tour.member.adapter.in;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import kori.tour.auth.token.JwtTokenProvider;
import kori.tour.member.adapter.in.oauth_key.ApiKeyDto;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.application.service.PlatformClient;
import kori.tour.member.domain.ActivityInfo;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.PlatformInfo;
import kori.tour.member.domain.PlatformProfile;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
class OauthLoginController {

    private final ApiKeyResolver apiKeyResolver;
    private final PlatformClient platformClient;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${service-url}")
    private String serviceURL;

    @GetMapping("/api/login/oauth/{platformName}") // 플랫폼으로부터 요청을 받는 부분 -> 이건 내가 설정
    public String listenRedirectRequestFromPlatform(@RequestParam("code") String authorizationCode, @PathVariable String platformName) {
        ApiKeyDto apiKeyDto = apiKeyResolver.resolveApiKey(platformName);
        String accessToken = platformClient.getAccessToken(authorizationCode, apiKeyDto);
        PlatformProfile platformProfile = platformClient.getPlatformProfileFromPlatform(accessToken, apiKeyDto, platformName);
        Optional<Member> m = memberRepository.findByPlatformProfile(platformProfile.getPlatformType(), platformProfile.getPlatformPk(), platformProfile.getPlatformEmail());
        Member member = m.orElseGet(() -> null);

        if (member == null) {
            member = memberRepository.save(Member.builder()
                    .emailSubscribe(true)
                    .activityInfo(new ActivityInfo(LocalDateTime.now(), LocalDate.now()))
                    .platformInfo(new PlatformInfo(platformProfile.getPlatformType(), platformProfile.getPlatformPk(), platformProfile.getPlatformEmail()))
                    .build());
        }
        String jwtToken = jwtTokenProvider.createAccessToken(member.getId().toString());
        return "redirect:"+ serviceURL + "/auth/callback/sign-in?token=" + jwtToken;
    }

}

package kori.tour.member.adapter.in.oauth_key;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoogleApiKey {

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.google.access-token-uri}")
    private String accessTokenUri;

    @Value("${oauth2.google.profile-uri}")
    private String profileUri;


}

package kori.tour.member.adapter.in.oauth_key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyDto {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String accessTokenUri;
    private String profileUri;

}

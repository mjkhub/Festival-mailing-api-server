package kori.tour.member.adapter.in;

import org.springframework.stereotype.Component;

import kori.tour.member.adapter.in.oauth_key.ApiKeyDto;
import kori.tour.member.adapter.in.oauth_key.GoogleApiKey;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class ApiKeyResolver {

    private final GoogleApiKey googleApiKey;

    public ApiKeyDto resolveApiKey(String platformName){
        if(("google").equals(platformName))
            return new ApiKeyDto(googleApiKey.getClientId(), googleApiKey.getClientSecret(), googleApiKey.getRedirectUri(), googleApiKey.getAccessTokenUri(), googleApiKey.getProfileUri());
        else return new ApiKeyDto();
    }


}

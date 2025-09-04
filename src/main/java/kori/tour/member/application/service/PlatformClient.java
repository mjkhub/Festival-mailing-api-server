package kori.tour.member.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import kori.tour.member.adapter.in.oauth_key.ApiKeyDto;
import kori.tour.member.domain.PlatformProfile;
import kori.tour.member.domain.PlatformType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PlatformClient {

    private RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(String authorizationCode, ApiKeyDto apiKeyDto){

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", apiKeyDto.getClientId());
        body.add("client_secret", apiKeyDto.getClientSecret());
        body.add("redirect_uri", apiKeyDto.getRedirectUri());
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(body, headers);

        ResponseEntity<JsonNode> stringResponseEntity = restTemplate.exchange(apiKeyDto.getAccessTokenUri(), HttpMethod.POST,entity, JsonNode.class);

        return stringResponseEntity.getBody().get("access_token").asText();

    }


    public PlatformProfile getPlatformProfileFromPlatform(String accessToken, ApiKeyDto apiKeyDto, String platformName){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        JsonNode body = restTemplate.exchange(apiKeyDto.getProfileUri(), HttpMethod.GET, entity, JsonNode.class).getBody();

        PlatformType platformType = PlatformType.getPlatformType(platformName);

        return platformType.parseToProfile(body);
    }


}

package kori.tour.member.domain;

import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;

public enum PlatformType {
    GOOGLE("google", (body) -> {
        JsonNode emailAddress = body.get("emailAddresses").get(0);
        String platformPk = emailAddress.get("metadata").get("source").get("id").asText();
        String platformEmail = emailAddress.get("value").asText();
        return new PlatformProfile(platformPk, platformEmail);
    }),
    YAHOO("yahoo", (body) -> {
        String platformPk = body.get("sub").asText();
        String platformEmail = body.get("email").asText();
        return new PlatformProfile(platformPk, platformEmail);
    }),
    EMAIL("kakao", null);

    @Getter
    private String platformName;
    private Function<JsonNode, PlatformProfile> parser;

    /**
     * Constructs a PlatformType enum constant with the specified platform name and profile parser.
     *
     * @param platformName the name identifying the authentication platform
     * @param parser a function that parses a JsonNode into a PlatformProfile for this platform type
     */
    PlatformType(String platformName, Function<JsonNode, PlatformProfile> parser) {
        this.platformName = platformName;
        this.parser = parser;
    }

    /**
     * Returns the {@code PlatformType} corresponding to the given platform name.
     *
     * @param platformName the name of the platform to look up
     * @return the matching {@code PlatformType}
     * @throws IllegalArgumentException if no matching platform type is found
     */
    public static PlatformType getPlatformType(String platformName){
        for (PlatformType pn : PlatformType.values()) {
            if(pn.platformName.equals(platformName))
                return pn;
        }
        throw new IllegalArgumentException("Wrong platform name");
    }

    /**
     * Parses the provided JSON node into a {@link PlatformProfile} using the platform-specific parser.
     *
     * The resulting profile will have its platform type set to this enum instance.
     *
     * @param body the JSON node containing user profile data for this platform
     * @return a {@link PlatformProfile} populated with data from the JSON node and associated with this platform type
     * @throws NullPointerException if no parser is defined for this platform type
     */
    public PlatformProfile parseToProfile(JsonNode body){
        PlatformProfile profile = this.parser.apply(body);
        profile.setPlatformType(this);
        return profile;
    }

}

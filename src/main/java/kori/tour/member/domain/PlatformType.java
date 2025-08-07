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
	}), YAHOO("yahoo", (body) -> {
		String platformPk = body.get("sub").asText();
		String platformEmail = body.get("email").asText();
		return new PlatformProfile(platformPk, platformEmail);
	}), KAKAO("kakao", null);

	@Getter
	private String platformName;

	private Function<JsonNode, PlatformProfile> parser;

	PlatformType(String platformName, Function<JsonNode, PlatformProfile> parser) {
		this.platformName = platformName;
		this.parser = parser;
	}

	public static PlatformType getPlatformType(String platformName) {
		for (PlatformType pn : PlatformType.values()) {
			if (pn.platformName.equals(platformName))
				return pn;
		}
		throw new IllegalArgumentException("Wrong platform name");
	}

	public PlatformProfile parseToProfile(JsonNode body) {
		PlatformProfile profile = this.parser.apply(body);
		profile.setPlatformType(this);
		return profile;
	}

}

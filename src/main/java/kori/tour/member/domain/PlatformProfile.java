package kori.tour.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class PlatformProfile {

	@Setter
	private PlatformType platformType;

	private String platformPk;

	private String platformEmail;

	public PlatformProfile(String platformPk, String platformEmail) { // oauth
		this.platformPk = platformPk;
		this.platformEmail = platformEmail;
	}

}

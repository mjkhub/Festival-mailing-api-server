package kori.tour.tour.domain;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Language {

	KOREAN("국문", "Korean", "KorService1");

	// ENGLISH("영문", "English", "EngService1"), SIMPLIFIED_CHINESE("중문 간체", "Simplified
	// Chinese",
	// "ChsService1"),
	// TRADITIONAL_CHINESE("중문 번체", "Traditional Chinese", "ChtService1"), JAPANESE("일문",
	// "Japanese", "JpnService1"),
	//
	// GERMAN("독어", "German", "GerService1"), FRENCH("불어", "French", "FreService1"),
	// SPANISH("서어", "Spanish", "SpnService1"), RUSSIAN("노어", "Russian", "RusService1");

	private final String krName;

	private final String egName;

	private final String path;

	Language(String krName, String egName, String path) {
		this.krName = krName;
		this.egName = egName;
		this.path = path;
	}

	public static Language getLanguageByEgName(String egName) {
		return Arrays.stream(Language.values())
				.filter(e->e.getEgName().equals(egName))
				.findAny()
				.orElseThrow( ()-> new IllegalArgumentException("Wrong eg Name"));
	}

}

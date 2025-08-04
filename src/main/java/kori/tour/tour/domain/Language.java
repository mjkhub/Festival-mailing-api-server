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

	/**
	 * Constructs a Language enum constant with the specified Korean name, English name, and path identifier.
	 *
	 * @param krName the Korean name of the language
	 * @param egName the English name of the language
	 * @param path the path identifier associated with the language
	 */
	Language(String krName, String egName, String path) {
		this.krName = krName;
		this.egName = egName;
		this.path = path;
	}

	/**
	 * Returns the {@code Language} enum constant that matches the specified English name.
	 *
	 * @param egName the English name of the language to look up
	 * @return the {@code Language} constant with the given English name
	 * @throws IllegalArgumentException if no matching language is found
	 */
	public static Language getLanguageByEgName(String egName) {
		return Arrays.stream(Language.values())
				.filter(e->e.getEgName().equals(egName))
				.findAny()
				.orElseThrow( ()-> new IllegalArgumentException("Wrong eg Name"));
	}

}

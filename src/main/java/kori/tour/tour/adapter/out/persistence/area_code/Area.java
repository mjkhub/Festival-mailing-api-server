package kori.tour.tour.adapter.out.persistence.area_code;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Area(@JsonProperty("code") String areaCode, String name,
		@JsonProperty("subRegions") List<SubArea> subRegions) {
}

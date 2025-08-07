package kori.tour.common.area_code;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubArea(@JsonProperty("code") String sigunguCode, String name) {
}

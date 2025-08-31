package kori.tour.global.data.area_code;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubArea(@JsonProperty("code") String sigunGuCode, String name) {
}

package kori.tour.tour.adapter.out.persistence.area_code;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubArea(@JsonProperty("code") String sigunGuCode, String name) {
}

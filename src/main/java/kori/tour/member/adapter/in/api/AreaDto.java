package kori.tour.member.adapter.in.api;

import java.util.List;

public record AreaDto (String areaCode, String name, List<SubAreaDto> subAreas){
}

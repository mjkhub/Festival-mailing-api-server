package kori.tour.member.adapter.in.api.out;

import java.util.List;

public record AreaDto (String areaCode, String name, List<SubAreaDto> subAreas){
}

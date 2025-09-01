package kori.tour.tour.adapter.in.api;

import java.util.List;


public record TourAllDto(
        Long tourId,
        String title,
        List<String> keywords,
        List<String> images,
        Overview overview,
        List<Info> detailInfo,
        Directions directions) {


    public record Overview(
            String eventStartDate,
            String eventEndDate,
            String businessHours,
            String expectedDuration,
            String cost
    ) {}

    public record Info(String infoName, String infoText) {}

    // 오시는 길(이메일 템플릿 변수와 매핑)
    public record Directions(
            String roadAddress,
            String eventPlace,
            String telephone) {}
}

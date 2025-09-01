package kori.tour.tour.adapter.in.api;

import java.util.List;


public record TourAllDto(
        Long tourId,
        String title,
        List<String> keywords,
        List<String> images,
        Overview overview,
        Detail detail,
        Directions directions) {


    public record Overview(
            String eventStartDate,
            String eventEndDate,
            String businessHours,
            String expectedDuration,
            String cost
    ) {}

    public record Detail(
            List<Info> infoList
    ) {}

    public record Info(String infoName, String infoText) {}

    // 오시는 길(이메일 템플릿 변수와 매핑)
    public record Directions(
            String roadAddress,
            String eventPlace,
            String telephone) {}
}

package kori.tour.member.adapter.in.api.out;

import java.time.LocalDate;
import java.util.List;

public record TourBasicDto(
        Long tourId,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String area,
        String sigunGu,
        String mainImageUrl,
        List<String> keywords
) {}

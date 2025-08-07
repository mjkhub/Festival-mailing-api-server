package kori.tour.email.application.updater.parser.dto;

import java.util.List;

public record EmailTitleDto(String areaCode, String sigunGuCode, String title, List<String> keywordsOfTour) {
}

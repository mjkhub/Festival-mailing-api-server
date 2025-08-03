package kori.tour.tour.application.updater;

import kori.tour.tour.domain.Language;

public record ApiParam(Language language, int numberOfRow, int pageNo, String additionalParam) {
}

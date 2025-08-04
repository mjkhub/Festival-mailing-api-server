package kori.tour.tour.application.updater.dto;

import java.util.List;

public record TourApiResponse(List<NewTourDto> newToursEntity, List<NewTourDto> updatedToursEntity) {
}

package kori.tour.tour.application.updater.dto;

import java.util.List;

import kori.tour.tour.domain.Tour;

public record TourFilterResponse(List<Tour> newTours, List<Tour> updatedTours) {
}

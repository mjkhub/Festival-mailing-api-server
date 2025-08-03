package kori.tour.email.application.updater;

import java.util.List;
import java.util.Map;

import kori.tour.keyword.domain.Keyword;
import kori.tour.tour.application.updater.dto.NewTourDto;

public record EmailSendEvent(Map.Entry<NewTourDto, List<Keyword>> entry) {

}

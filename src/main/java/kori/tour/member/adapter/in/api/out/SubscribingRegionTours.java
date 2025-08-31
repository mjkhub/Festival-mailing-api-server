package kori.tour.member.adapter.in.api.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
public class SubscribingRegionTours {

    private final MetaData metaData;

    private final List<TourBasicDto> tours;

    public SubscribingRegionTours(int contentSize, boolean hasNext, List<TourBasicDto> tours) {
        this.metaData = new MetaData(contentSize, hasNext);
        this.tours = tours;
    }

    @Getter
    @AllArgsConstructor
    static class MetaData{
        int contentSize;
        boolean hasNext;
    }
}

package kori.tour.member.adapter.in.api.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;


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

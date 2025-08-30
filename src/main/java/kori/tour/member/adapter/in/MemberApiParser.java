package kori.tour.member.adapter.in;

import kori.tour.global.data.area_code.Area;
import kori.tour.member.adapter.in.api.AreaDto;
import kori.tour.member.adapter.in.api.SubAreaDto;
import kori.tour.member.adapter.in.api.SubscriptionsResponse;
import kori.tour.member.domain.Subscription;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class MemberApiParser {

    public SubscriptionsResponse mapToSubscriptionResponse(List<Area> areaCodeList, Set<Subscription> subscriptions) {

        // 1. 빠른 조회를 위해 (areaCode, sigunGuCode) -> boolean 맵 구성
        Set<String> subscribedKeys = subscriptions.stream()
                .map(s -> s.getAreaCode() + ":" + s.getSigunGuCode())
                .collect(Collectors.toSet());

        // 2. 전체 지역 + 구독 여부 붙이기
        List<AreaDto> areaDtos = areaCodeList.stream()
                .map(area -> new AreaDto(
                        area.areaCode(),
                        area.name(),
                        area.subRegions().stream()
                                .map(sub -> new SubAreaDto(
                                        sub.sigunGuCode(),
                                        sub.name(),
                                        subscribedKeys.contains(area.areaCode() + ":" + sub.sigunGuCode())
                                ))
                                .toList()
                ))
                .toList();

        // 3. 최종 응답
        return new SubscriptionsResponse(areaDtos);
    }


}

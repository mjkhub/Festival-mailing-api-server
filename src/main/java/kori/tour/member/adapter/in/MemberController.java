package kori.tour.member.adapter.in;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import kori.tour.global.data.area_code.Area;
import kori.tour.global.data.area_code.AreaCodeRegistry;
import kori.tour.member.adapter.in.api.SubscriptionsResponse;
import kori.tour.member.application.port.MemberUseCase;
import kori.tour.member.domain.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value= "/api/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {

    private final MemberUseCase memberUseCase;
    private final AreaCodeRegistry areaCodeRegistry;
    private final MemberApiParser memberApiParser;
//    @GetMapping("/")
//    public ResponseEntity<MemberLoginHome> memberLoginHome(HttpServletRequest servletRequest){
//
//    }

    @Operation(summary = "전체 지역 목록 조회", description = "회원이 구독하는 지역을 표시하여 전체 지역 목록을 반환합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionsResponse.class))),
    })
    @GetMapping("/subscriptions")
    public ResponseEntity<SubscriptionsResponse> memberSubscriptions(HttpServletRequest servletRequest){
        List<Area> areaCodeList = areaCodeRegistry.getAreaCodeList();
        Set<Subscription> subscriptions = memberUseCase.getSubscriptions(1L);
        SubscriptionsResponse subscriptionsResponse = memberApiParser.mapToSubscriptionResponse(areaCodeList, subscriptions);
        return ResponseEntity.ok().body(subscriptionsResponse);
    }



//    @DeleteMapping("/")
//    public ResponseEntity<Void> memberSubscriptions(HttpServletRequest servletRequest){
//
//
//        return null;
//    }


}
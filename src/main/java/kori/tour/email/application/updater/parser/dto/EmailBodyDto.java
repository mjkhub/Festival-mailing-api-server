package kori.tour.email.application.updater.parser.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Getter
@AllArgsConstructor
public class EmailBodyDto {
    private String mainImageUrl;
    private String title;
    private LocalDate eventStartDate;
    private LocalDate eventEndDate;
    private String playTime; //운영 시간
    private String spendTimeFestival; // 예상 소요시간 -> 빈값이 많음
    private String useTimeFestival; //비용
    private List<String> keywords; // 행사 키워드

    private List<InfoItem> infoList; // 구체적인 정보 - 반복문으로 ~~

    private String roadAddress; // 주소
    private String eventPlace; //세부 장소
    private String telephone; //연락처

    @Getter
    @AllArgsConstructor
    public static class InfoItem {
        private String infoName;
        private String infoText;
    }
}

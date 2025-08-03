package kori.tour.common.area_code;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class AreaCodeRepository {

    @Value("classpath:/files/area/area-code.json")
    private Resource areaCodeResource;

    @Getter
    private List<Area> areaCodeList;

    private final AreaCodeParser areaCodeParser;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try (InputStream is = areaCodeResource.getInputStream()) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            areaCodeList = areaCodeParser.parseToAreaCode(json);
            log.info("======== Json 파일로부터 지역 코드, 시군구 코드 읽어오기 성공 ========");
        } catch (IOException e) {
            throw new IllegalStateException("지역 코드 파일을 읽는 데 실패했습니다", e);
        }
    }

    // Todo 1.지역코드만  2.지역 코드를 클릭하면 시군구 코드를 함께 보내주는~~ 3. 예외처리 4. 폴더를 어디에 정리할지

    public String getAreaName(String areaCode) {
        return areaCodeList.stream()
                .filter(ac-> ac.areaCode().equals(areaCode))
                .map(Area::name)
                .findAny()
                .orElseThrow();
    }

    public String getSigunGuName(String areaCode, String sigunguCode) {
        List<SubArea> subAreas = areaCodeList.stream()
                .filter(ac -> ac.areaCode().equals(areaCode))
                .map(Area::subRegions)
                .findAny()
                .orElseThrow();

        return subAreas.stream()
                .filter( sr->sr.sigunguCode().equals(sigunguCode))
                .map(SubArea::name)
                .findAny()
                .orElseThrow();
    }


}

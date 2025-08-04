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

    /**
     * Loads area and sub-area codes from the configured JSON resource file at application startup.
     *
     * Parses the JSON content into a list of area objects and stores them for later retrieval.
     * Throws an {@link IllegalStateException} if the resource file cannot be read.
     */
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

    /**
     * Returns the name of the area corresponding to the given area code.
     *
     * @param areaCode the code identifying the area
     * @return the name of the area matching the provided code
     * @throws NoSuchElementException if no area with the specified code is found
     */

    public String getAreaName(String areaCode) {
        return areaCodeList.stream()
                .filter(ac-> ac.areaCode().equals(areaCode))
                .map(Area::name)
                .findAny()
                .orElseThrow();
    }

    /**
     * Returns the name of the sub-area (sigungu) corresponding to the given area code and sigungu code.
     *
     * @param areaCode the code identifying the area
     * @param sigunguCode the code identifying the sub-area within the area
     * @return the name of the matching sub-area
     * @throws NoSuchElementException if no matching area or sub-area is found
     */
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

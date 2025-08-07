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
		}
		catch (IOException e) {
			throw new IllegalStateException("지역 코드 파일을 읽는 데 실패했습니다", e);
		}
	}

	// Todo 이 클래스 어느 폴더에 정리할지 잘 모르겠음

	public String getAreaName(String areaCode) {
		return areaCodeList.stream()
			.filter(ac -> ac.areaCode().equals(areaCode))
			.map(Area::name)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("Area code not found: " + areaCode));
	}

	public String getSigunGuName(String areaCode, String sigunguCode) {
		List<SubArea> subAreas = areaCodeList.stream()
			.filter(ac -> ac.areaCode().equals(areaCode))
			.map(Area::subRegions)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("Area code not found: " + areaCode));

		return subAreas.stream()
			.filter(sr -> sr.sigunguCode().equals(sigunguCode))
			.map(SubArea::name)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException(
					String.format("Sigungu code '%s' not found for area code '%s'", sigunguCode, areaCode)));
	}

}

package kori.tour.tour.adapter.out.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import kori.tour.global.exception.AsyncProcessingException;
import kori.tour.global.exception.NotFoundException;
import kori.tour.global.exception.code.ErrorCode;
import kori.tour.tour.adapter.out.persistence.area_code.Area;
import kori.tour.tour.adapter.out.persistence.area_code.AreaCodeParser;
import kori.tour.tour.adapter.out.persistence.area_code.SubArea;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AreaCodeMemoryRepository {

	@Value("classpath:/files/area/area-code.json")
	private Resource areaCodeResource;

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
			throw new AsyncProcessingException(ErrorCode.AREA_CODE_FILE);
		}
	}

	public String getAreaName(String areaCode) {
		return areaCodeList.stream()
				.filter(ac -> ac.areaCode().equals(areaCode))
				.map(Area::name)
				.findAny()
				.orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));
	}

	public String getSigunGuName(String areaCode, String sigunGuCode) {
		List<SubArea> subAreas = areaCodeList.stream()
				.filter(ac -> ac.areaCode().equals(areaCode))
				.map(Area::subRegions)
				.findAny()
				.orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

		return subAreas.stream()
				.filter(sr -> sr.sigunGuCode().equals(sigunGuCode))
				.map(SubArea::name)
				.findAny()
				.orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));
	}

}

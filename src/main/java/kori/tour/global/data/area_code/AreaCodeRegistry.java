package kori.tour.global.data.area_code;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import kori.tour.global.exception.AsyncProcessingException;
import kori.tour.global.exception.NotFoundException;
import kori.tour.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AreaCodeRegistry {

	@Value("classpath:/files/area/area-code.json")
	private Resource areaCodeResource;

	private List<Area> areaCodeList;

	private final AreaCodeParser areaCodeParser;

	@PostConstruct
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

	public List<Area> getAreaCodeList() {
		return Collections.unmodifiableList(areaCodeList);
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

	public void validateAreaAndSigunGuCode(String areaCode, String sigunGuCode) {
		// areaCode 존재 여부 확인
		Area area = areaCodeList.stream()
				.filter(ac -> ac.areaCode().equals(areaCode))
				.findAny()
				.orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));

		// sigunGuCode 존재 여부 확인
		area.subRegions().stream()
				.filter(sr -> sr.sigunGuCode().equals(sigunGuCode))
				.findAny()
				.orElseThrow(() -> new NotFoundException(ErrorCode.AREA_NOT_FOUND));
	}

}

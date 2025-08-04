package kori.tour.tour.application.updater.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kori.tour.tour.application.updater.dto.TourMetaData;
import kori.tour.tour.domain.Language;
import kori.tour.tour.domain.dto.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TourApiResponseParser {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public TourMetaData mapToTotalPage(String jsonResponse, Language language) {

		int numberOfRows = 0;
		int pageNo = 1;
		int totalCount = 0;

		TourMetaData tourMetaData = null;
		try {
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode bodyNode = rootNode.path("response").path("body");
			numberOfRows = bodyNode.path("numOfRows").asInt();
			pageNo = bodyNode.path("pageNo").asInt();
			totalCount = bodyNode.path("totalCount").asInt();
			int totalPage = (int) Math.ceil((double) totalCount / numberOfRows); // total
																					// page
			tourMetaData = new TourMetaData(numberOfRows, totalCount, totalPage);

		}
		catch (JsonProcessingException e) { // xml response
			log.warn("/searchFestival1 언어={} 응답 Metadata 파싱중 에러 발생 response = {}", language.getKrName(), jsonResponse,
					e);
			return new TourMetaData(0, 0, 0);
		}

		return tourMetaData;
	}

	public List<TourResponse> mapToTourCreateList(String jsonResponse) {

		List<TourResponse> tourResponseList = new ArrayList<>();

		try {
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

			if (itemsNode.isArray()) {
				for (JsonNode itemNode : itemsNode) {

					TourResponse tourResponse = new TourResponse(itemNode.path("addr1").asText(),
							itemNode.path("addr2").asText(), itemNode.path("areacode").asText(),
							itemNode.path("sigungucode").asText(), itemNode.path("contentid").asText(),
							itemNode.path("contenttypeid").asText(),
							TourTimeUtil.parseLocalDate(itemNode.path("eventstartdate").asText()),
							TourTimeUtil.parseLocalDate(itemNode.path("eventenddate").asText()),
							itemNode.path("firstimage").asText(), itemNode.path("mapx").asText(),
							itemNode.path("mapy").asText(), itemNode.path("mlevel").asText(),
							TourTimeUtil.parseToLocalDateTime(itemNode.path("modifiedtime").asText()),
							itemNode.path("tel").asText(), itemNode.path("title").asText());
					tourResponseList.add(tourResponse);
				}
			}

		}
		catch (JsonProcessingException e) { // xml response
			log.warn("/searchFestival1 응답 파싱중 에러 발생 response = {}", jsonResponse, e);
		}
		return tourResponseList;
	}

	/**
	 * Tour - TourDetail은 OneToOne 이지만, 통일성을 위해 List로 작업
	 * */
	public List<TourDetailResponse> mapToTourDetailCreate(String jsonResponse) {
		List<TourDetailResponse> tourDetailResponseList = new ArrayList<>();
		try {
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
			if (itemsNode.isArray()) {
				for (JsonNode itemNode : itemsNode) {
					TourDetailResponse tourDetailResponse = new TourDetailResponse(itemNode.path("agelimit").asText(),
							itemNode.path("bookingplace").asText(), itemNode.path("discountinfofestival").asText(),
							itemNode.path("eventhomepage").asText(), itemNode.path("eventplace").asText(),
							itemNode.path("placeinfo").asText(), itemNode.path("playtime").asText(),
							itemNode.path("program").asText(), itemNode.path("spendtimefestival").asText(),
							itemNode.path("sponsor1").asText(), itemNode.path("sponsor1tel").asText(),
							itemNode.path("subevent").asText(), itemNode.path("usetimefestival").asText());
					tourDetailResponseList.add(tourDetailResponse);
				}
			}
			return tourDetailResponseList; // 항상 1개이긴 하지만, 코드 규칙을 위해서 이렇게 작성
		}
		catch (JsonProcessingException e) { // xml response
			log.warn("/detailIntro1 응답 파싱중 에러 발생 response = {}", jsonResponse, e);
			return new ArrayList<>();
		}
	}

	public List<TourRepeatResponse> mapToTourRepeatCreateList(String jsonResponse) {
		List<TourRepeatResponse> tourRepeatList = new ArrayList<>();

		try {
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
			if (itemsNode.isArray()) {
				for (JsonNode itemNode : itemsNode) {
					TourRepeatResponse repeatCreate = new TourRepeatResponse(itemNode.path("serialnum").asText(""),
							itemNode.path("infoname").asText(""), itemNode.path("infotext").asText(""));
					tourRepeatList.add(repeatCreate);
				}
			}
		}
		catch (JsonProcessingException e) { // xml response
			log.warn("/detailInfo1 응답 파싱중 에러 발생 response = {}", jsonResponse, e);
		}

		return tourRepeatList;
	}

	public List<TourImageResponse> mapToTourImageCreateList(String jsonResponse) {
		List<TourImageResponse> tourImageList = new ArrayList<>();
		try {
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

			if (itemsNode.isArray()) {
				for (JsonNode itemNode : itemsNode) {
					TourImageResponse imageCreate = new TourImageResponse(itemNode.path("originimgurl").asText(""),
							itemNode.path("smallimageurl").asText(""), itemNode.path("imgname").asText(""),
							itemNode.path("serialnum").asText(""));
					tourImageList.add(imageCreate);
				}
			}
		}
		catch (JsonProcessingException e) { // xml response
			log.warn("/detailImage1 응답 파싱중 에러 발생 response = {}", jsonResponse, e);
		}

		return tourImageList;
	}

}

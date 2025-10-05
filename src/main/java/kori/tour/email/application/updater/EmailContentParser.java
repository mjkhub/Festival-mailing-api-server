package kori.tour.email.application.updater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.springframework.stereotype.Component;

import kori.tour.email.application.updater.dto.EmailBodyDto;
import kori.tour.email.application.updater.dto.EmailTitleDto;
import kori.tour.global.data.area_code.AreaCodeRegistry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailContentParser {

	private final AreaCodeRegistry areaCodeRegistry;

	private final TemplateEngine templateEngine;

	public String parseToEmailTitle(EmailTitleDto emailTitleDto) {
		String areaCode = emailTitleDto.areaCode();
		String sigunGuCode = emailTitleDto.sigunGuCode();
		String title = emailTitleDto.title();

		String areaName = areaCodeRegistry.getAreaName(areaCode);
		String sigunGuName = areaCodeRegistry.getSigunGuName(areaCode, sigunGuCode);

		String keywords = String.join(", ", emailTitleDto.keywordsOfTour());
		return String.format("[%s-%s]: %s - %s", areaName, sigunGuName, title, keywords);
	}

	public String parseToEventEmailHtml(EmailBodyDto emailBodyDto) {
		Context context = new Context();
		context.setVariable("mainImageUrl", emailBodyDto.getMainImageUrl());
		context.setVariable("title", emailBodyDto.getTitle());
		context.setVariable("eventStartDate", emailBodyDto.getEventStartDate());
		context.setVariable("eventEndDate", emailBodyDto.getEventEndDate());
		context.setVariable("playTime", emailBodyDto.getPlayTime());
		context.setVariable("spendTimeFestival", emailBodyDto.getSpendTimeFestival());
		context.setVariable("useTimeFestival", emailBodyDto.getUseTimeFestival());
		context.setVariable("keywords", emailBodyDto.getKeywords());
		context.setVariable("infoList", emailBodyDto.getInfoList());
		context.setVariable("roadAddress", emailBodyDto.getRoadAddress());
		context.setVariable("eventPlace", emailBodyDto.getEventPlace());
		context.setVariable("telephone", emailBodyDto.getTelephone());

		return templateEngine.process("email-body", context);
	}

	/**
	 * 실제 사용자에게 전송되는 이메일과 띄어쓰기 하나 정도 차이가 있음.
	 * 여기에 시간을 더 쏟는 것 보다 빠르게 치고가는게 더 효율적이라서 일단 이 정도로만~
	 * minify 전: 약 2100바이트 minify 후: 약 1800바이트 -> 약 300 바이트 감소
	 * */
	public String minifyHtml(String html){
		Document doc = Jsoup.parse(html);
		doc.outputSettings().prettyPrint(false);

		Element body = doc.body();
		String innerHtml = body.html();

		innerHtml = innerHtml.replaceAll(">\\s+<", "><").trim();

        return "<body>" + innerHtml + "</body>";
	}

}

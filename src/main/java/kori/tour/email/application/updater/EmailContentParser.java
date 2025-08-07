package kori.tour.email.application.updater;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.springframework.stereotype.Component;

import kori.tour.email.application.updater.dto.EmailBodyDto;
import kori.tour.email.application.updater.dto.EmailTitleDto;
import kori.tour.tour.adapter.out.persistence.AreaCodeMemoryRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailContentParser {

	private final AreaCodeMemoryRepository areaCodeMemoryRepository;

	private final TemplateEngine templateEngine;

	public String parseToEmailTitle(EmailTitleDto emailTitleDto) {
		String areaCode = emailTitleDto.areaCode();
		String sigunGuCode = emailTitleDto.sigunGuCode();
		String title = emailTitleDto.title();

		String areaName = areaCodeMemoryRepository.getAreaName(areaCode);
		String sigunGuName = areaCodeMemoryRepository.getSigunGuName(areaCode, sigunGuCode);

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

}

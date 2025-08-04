package kori.tour.email.application.updater.parser;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.springframework.stereotype.Component;

import kori.tour.common.area_code.AreaCodeRepository;
import kori.tour.email.application.updater.parser.dto.EmailBodyDto;
import kori.tour.email.application.updater.parser.dto.EmailTitleDto;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class EmailContentParser {

    private final AreaCodeRepository areaCodeRepository;
    private final TemplateEngine templateEngine;

    /**
     * Constructs an email title string for a tour event using area and district names, event title, and keywords.
     *
     * The title is formatted as: "[AreaName-SigunGuName]: Title - keywords".
     *
     * @param emailTitleDto the DTO containing area code, district code, title, and keywords for the event
     * @return the formatted email title string
     */
    public String parseToEmailTitle(EmailTitleDto emailTitleDto){
        String areaCode = emailTitleDto.areaCode();
        String sigunGuCode = emailTitleDto.sigunGuCode();
        String title = emailTitleDto.title();

        String areaName = areaCodeRepository.getAreaName(areaCode);
        String sigunGuName = areaCodeRepository.getSigunGuName(areaCode, sigunGuCode);

        String keywords = String.join(", ", emailTitleDto.keywordsOfTour());
        return String.format("[%s-%s]: %s - %s", areaName, sigunGuName, title, keywords);
    }

    /**
     * Generates an HTML email body for a tour event using the provided event details.
     *
     * Populates a Thymeleaf context with information from the given {@code EmailBodyDto} and processes the "email-body" template to produce the final HTML content.
     *
     * @param emailBodyDto the DTO containing event details to be included in the email body
     * @return the generated HTML string for the event email
     */
    public String generateEventEmailHtml(EmailBodyDto emailBodyDto) {
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

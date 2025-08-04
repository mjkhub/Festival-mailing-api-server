package kori.tour.keyword.application.updater;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.keyword.application.port.out.KeywordExtractingPort;
import kori.tour.keyword.application.updater.parser.AiModelResponseParser;
import kori.tour.keyword.application.updater.parser.FestivalDocument;
import kori.tour.keyword.application.updater.parser.FestivalDocumentParser;
import kori.tour.keyword.domain.Keyword;
import kori.tour.tour.application.updater.dto.NewTourDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeywordExtractService {

    private final TourAiModelClient tourAiModelClient;
    private final FestivalDocumentParser festivalDocumentParser;
    private final AiModelResponseParser aiModelResponseParser;
    private final PromptBuilder promptBuilder;
    private final KeywordExtractingPort keywordExtractingPort;


    public List<String> extractKeywords(NewTourDto newTourDto){
        FestivalDocument festivalDocument = FestivalDocument.createFestivalDocument(newTourDto);
        String jsonDocument = festivalDocumentParser.mapToJson(festivalDocument);
        Prompt prompt = promptBuilder.buildKeywordPrompt(jsonDocument);
        String aiResponse = tourAiModelClient.call(prompt);
        return aiModelResponseParser.mapToKeywords(aiResponse);
    }

    @Transactional
    public Map.Entry<NewTourDto,List<Keyword>> saveKeywords(NewTourDto newTourDto, List<Keyword> keywordsOfTour){
        keywordExtractingPort.saveKeyword(newTourDto.getTour(),keywordsOfTour);
        return Map.entry(newTourDto,keywordsOfTour);
    }

}

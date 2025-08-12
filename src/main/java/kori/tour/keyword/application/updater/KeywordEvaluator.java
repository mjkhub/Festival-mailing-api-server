package kori.tour.keyword.application.updater;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.Evaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class KeywordEvaluator implements Evaluator {

    @Value("classpath:/files/keyword/tour-keywords.st")
    private Resource keywordFile;

    private Set<String> keywords;

    @PostConstruct
    private void init() throws IOException{
        String content = new String(keywordFile.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        keywords = Arrays.stream(content.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public EvaluationResponse evaluate(EvaluationRequest evaluationRequest) {
        String responseContent = evaluationRequest.getResponseContent();
        String trimmed = responseContent.substring(1, responseContent.length() - 1); // 대괄호 제거
        List<String> parsed = Arrays.stream(trimmed.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        boolean pass = keywords.containsAll(parsed) && parsed.size() == new HashSet<>(parsed).size();

        return new EvaluationResponse(pass, 0,null,null);
    }
}

package kori.tour.keyword.application.updater;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;


@Service
@Component
public class PromptBuilder {

	@Value("classpath:/files/prompts/keyword-extract-template.st")
	private Resource keywordExtractPromptTemplate;

	@Value("classpath:/files/keyword/tour-keywords.st")
	private Resource keywords;

	private PromptTemplate promptTemplate;

	@PostConstruct
	private void init() {
		this.promptTemplate = new PromptTemplate(keywordExtractPromptTemplate);
	}

	public Prompt buildKeywordPrompt(String tourDocument) {
		Map<String, Object> promptParameters = new HashMap<>();
		promptParameters.put("document", tourDocument);
		promptParameters.put("keywords", keywords);
		return promptTemplate.create(promptParameters);
	}

}

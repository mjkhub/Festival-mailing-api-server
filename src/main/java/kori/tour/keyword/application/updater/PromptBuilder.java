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

	/**
	 * Initializes the prompt template using the injected keyword extraction template resource.
	 */
	@PostConstruct
	private void init() {
		this.promptTemplate = new PromptTemplate(keywordExtractPromptTemplate);
	}

	/**
	 * Constructs a prompt for keyword extraction using the provided tour document and predefined keywords.
	 *
	 * @param tourDocument the text of the tour document to extract keywords from
	 * @return a Prompt object configured with the tour document and keywords
	 */
	public Prompt buildKeywordPrompt(String tourDocument) {
		Map<String, Object> promptParameters = new HashMap<>();
		promptParameters.put("document", tourDocument);
		promptParameters.put("keywords", keywords);
		return promptTemplate.create(promptParameters);
	}

}

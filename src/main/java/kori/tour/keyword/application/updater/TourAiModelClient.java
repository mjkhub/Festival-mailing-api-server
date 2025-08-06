package kori.tour.keyword.application.updater;

import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourAiModelClient {

	private final OpenAiChatModel openAiChatModel;

	public String call(Prompt prompt) {
		// Generation의 개수는 Open API 서버에서 1개로 처리한다고 한다.
        return openAiChatModel.call(prompt).getResult().getOutput().getContent();
	}

}

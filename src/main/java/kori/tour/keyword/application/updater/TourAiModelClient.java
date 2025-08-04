package kori.tour.keyword.application.updater;

import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourAiModelClient {

	private final OpenAiChatModel openAiChatModel;

	/**
	 * Sends a prompt to the OpenAI chat model and returns the generated response content.
	 *
	 * If the model does not return a result, an empty string is returned.
	 *
	 * @param prompt the prompt to send to the chat model
	 * @return the generated response content, or an empty string if no result is returned
	 */
	public String call(Prompt prompt) {
		// Generation의 개수는 Open API 서버에서 1개로 처리한다고 한다.
		Generation result = openAiChatModel.call(prompt).getResult();
		if(result != null) return result.getOutput().getContent();
		else return ""; // Todo: API 서버에서 응답을 못받았을 때 처리
	}

}

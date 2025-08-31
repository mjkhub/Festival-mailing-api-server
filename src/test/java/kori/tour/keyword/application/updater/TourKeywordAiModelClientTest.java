package kori.tour.keyword.application.updater;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
class TourKeywordAiModelClientTest {

    @Autowired
    TourKeywordAiModelClient tourAiModelClient;

    @MockBean
    OpenAiChatModel openAiChatModel;

    @Test
    @DisplayName("AI 모델 호출 시 예상된 키워드 결과를 성공적으로 반환한다")
    void call_success() {
        // given
        String expectedResponseContent = "{\"keywords\": [\"가족 여행\", \"힐링\", \"자연\"]}";
        Prompt prompt = new Prompt("문서에서 키워드를 추출해주세요.");

        Generation generation = new Generation(new AssistantMessage(expectedResponseContent));
        ChatResponse chatResponse = new ChatResponse(List.of(generation));

        Mockito.when(openAiChatModel.call(any(Prompt.class))).thenReturn(chatResponse);

        // when
        String actualResponse = tourAiModelClient.call(prompt);

        // then
        assertThat(actualResponse).isEqualTo(expectedResponseContent);
    }

}

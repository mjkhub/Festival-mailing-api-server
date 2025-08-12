package kori.tour.keyword.application.updater;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class KeywordEvaluatorTest {

    @Autowired
    private KeywordEvaluator keywordEvaluator;

    @Test
    @DisplayName("키워드 유효성 평가: 존재하는 키워드")
    void testEvaluateWithAllMatchingKeywords() {
        // given
        EvaluationRequest request = new EvaluationRequest("",null,"[영화, 게임]");

        // when
        EvaluationResponse response = keywordEvaluator.evaluate(request);

        // then
        Assertions.assertThat(response.isPass()).isTrue();
    }

    @Test
    @DisplayName("키워드 유효성 평가: 중복 키워드")
    void testEvaluateWithDuplicateKeywords() {
        // given
        EvaluationRequest request = new EvaluationRequest("",null,"[영화, 영화]");

        // when
        EvaluationResponse response = keywordEvaluator.evaluate(request);

        // then
        Assertions.assertThat(response.isPass()).isFalse();
    }

    @Test
    @DisplayName("키워드 유효성 평가: 존재하지 않는 키워드")
    void testEvaluateWithNonMatchingKeywords() {
        // given
        EvaluationRequest request = new EvaluationRequest("",null,"[unknown1, unknown2]");

        // when
        EvaluationResponse response = keywordEvaluator.evaluate(request);

        // then
        Assertions.assertThat(response.isPass()).isFalse();
    }


}

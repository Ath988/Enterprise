package com.bilgeadam.service;

import com.bilgeadam.dto.request.SubmitSurveyRequestDto;
import com.bilgeadam.dto.request.AnswerRequestDto;
import com.bilgeadam.dto.response.SurveyResponseDetailDto;
import com.bilgeadam.entity.SurveyResponse;
import com.bilgeadam.entity.Answer;
import com.bilgeadam.repository.SurveyResponseRepository;
import com.bilgeadam.exception.SurveyServiceException;
import com.bilgeadam.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyResponseService {
    private final SurveyResponseRepository surveyResponseRepository;
    private final AnswerService answerService;

    public SurveyResponse submitSurveyResponse(SubmitSurveyRequestDto dto) {
        SurveyResponse surveyResponse = SurveyResponse.builder()
                .userId(dto.getUserId())
                .surveyId(dto.getSurveyId())
                .submissionDate(LocalDateTime.now())
                .build();
        
        surveyResponse = surveyResponseRepository.save(surveyResponse);
        List<String> answerIds = createAnswersForResponse(dto.getAnswers(), surveyResponse.getId());
        surveyResponse.setAnswerIds(answerIds);
        
        return surveyResponseRepository.save(surveyResponse);
    }

    private List<String> createAnswersForResponse(List<AnswerRequestDto> answers, String surveyResponseId) {
        return answers.stream()
                .map(answerDto -> {
                    Answer answer = Answer.builder()
                            .questionId(answerDto.getQuestionId())
                            .surveyResponseId(surveyResponseId)
                            .answerText(answerDto.getAnswerText())
                            .selectedOptionId(answerDto.getSelectedOptionId())
                            .build();
                    
                    return answerService.save(answer).getId();
                })
                .collect(Collectors.toList());
    }

    public List<SurveyResponse> getUserResponses(Long userId) {
        return surveyResponseRepository.findAllByUserId(userId);
    }

    public SurveyResponseDetailDto getResponseWithDetails(String responseId) {
        SurveyResponse response = surveyResponseRepository.findById(responseId)
                .orElseThrow(() -> new SurveyServiceException(ErrorType.RESPONSE_NOTFOUND));

        List<Answer> answers = answerService.findAllByIdIn(response.getAnswerIds());

        return SurveyResponseDetailDto.builder()
                .surveyResponse(response)
                .answers(answers)
                .build();
    }
} 
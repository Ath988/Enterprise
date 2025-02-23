package com.bilgeadam.service;

import com.bilgeadam.dto.request.SubmitSurveyRequestDto;
import com.bilgeadam.dto.response.SurveyResponseDetailDto;
import com.bilgeadam.entity.SurveyResponse;
import com.bilgeadam.entity.Answer;
import com.bilgeadam.repository.AnswerRepository;
import com.bilgeadam.repository.SurveyResponseRepository;
import com.bilgeadam.exception.SurveyServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyResponseService {
    private final SurveyResponseRepository surveyResponseRepository;
    private final AnswerService answerService;
    private final JwtManager jwtManager;
    
    private Long validateTokenAndCheckRole(String token, String... expectedRoles) {
        token = token.replace("Bearer ", "");
        
        Long userId = jwtManager.validateToken(token)
                                .orElseThrow(() -> new SurveyServiceException(ErrorType.INVALID_TOKEN));
        
        if (!jwtManager.hasRole(token, expectedRoles)) {
            throw new SurveyServiceException(ErrorType.UNAUTHORIZED);
        }
        
        return userId;
    }
    
    @Transactional
    public Boolean submitSurveyResponse(String token, SubmitSurveyRequestDto dto) {
        try {
            // Anketi member ve staff rolleri cevaplanıdrabiliyor. (Sadece Staff da yapılabilir.)
            Long userId = validateTokenAndCheckRole(token, "MEMBER", "STAFF");
            
            SurveyResponse surveyResponse = SurveyResponse.builder()
                                                          .userId(userId)
                                                          .surveyId(dto.getSurveyId())
                                                          .submissionDate(LocalDateTime.now())
                                                          .build();
            
            List<String> answerIds = new ArrayList<>();
            
            dto.getAnswers().forEach(answerDto -> {
                Answer answer = Answer.builder()
                                      .questionId(answerDto.getQuestionId())
                                      .answerText(answerDto.getAnswerText())
                                      .selectedOptionId(answerDto.getSelectedOptionId())
                                      .surveyResponseId(surveyResponse.getId())
                                      .build();
                
                answerService.save(answer);
                answerIds.add(answer.getId());
            });
            
            surveyResponse.setAnswerIds(answerIds);
            surveyResponseRepository.save(surveyResponse);
            
            return true;
        } catch (SurveyServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Anketi yanıtlama hatası: ", e);
            throw new SurveyServiceException(ErrorType.SURVEY_SUBMIT_ERROR);
        }
    }
    // Member'ın tüm cevapları görebilmesi için.
    public List<SurveyResponseDetailDto> getSurveyResponses(String token, String surveyId) {
        validateTokenAndCheckRole(token, "MEMBER");
        
        List<SurveyResponse> responses = surveyResponseRepository.findAllBySurveyId(surveyId);
        
        return responses.stream()
                        .map(response -> {
                            List<Answer> answers = answerService.findAllByIds(response.getAnswerIds());
                            return SurveyResponseDetailDto.builder()
                                                          .surveyResponse(response)
                                                          .answers(answers)
                                                          .build();
                        })
                        .collect(Collectors.toList());
    }
    
    //Kullanıcının kendi cevaplarını görebilmesi için.
    public List<SurveyResponseDetailDto> getUserSurveyResponses(String token) {
        Long userId = validateTokenAndCheckRole(token, "MEMBER", "STAFF");
        
        List<SurveyResponse> responses = surveyResponseRepository.findAllByUserId(userId);
        
        return responses.stream()
                        .map(response -> {
                            List<Answer> answers = answerService.findAllByIds(response.getAnswerIds());
                            return SurveyResponseDetailDto.builder()
                                                          .surveyResponse(response)
                                                          .answers(answers)
                                                          .build();
                        })
                        .collect(Collectors.toList());
    }
}
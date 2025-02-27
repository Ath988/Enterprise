package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateSurveyRequestDto;
import com.bilgeadam.dto.response.QuestionDetailDto;
import com.bilgeadam.dto.response.SurveyDetailDto;
import com.bilgeadam.entity.Option;
import com.bilgeadam.entity.Question;
import com.bilgeadam.entity.Survey;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.SurveyServiceException;
import com.bilgeadam.manager.OrganisationManager;
import com.bilgeadam.repository.SurveyRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionService questionService;
    private final OptionService optionService;
    private final JwtManager jwtManager;
    private final OrganisationManager organisationManager;
    
    private Long validateTokenAndCheckRole(String token, String... expectedRoles) {
        token = token.replace("Bearer ", "");
        
        Long userId = jwtManager.validateToken(token)
                                .orElseThrow(() -> new SurveyServiceException(ErrorType.INVALID_TOKEN));
        
        // Rolleri kontrol ediyorum.
        if (!jwtManager.hasRole(token, expectedRoles)) {
            throw new SurveyServiceException(ErrorType.UNAUTHORIZED);
        }
        
        return userId;
    }
    
    @Transactional
    public Boolean createSurvey(String token, CreateSurveyRequestDto dto) {
        try {
            // Sadece MEMBER rolü anket oluşturabilir
            Long authId = validateTokenAndCheckRole(token, "MEMBER");
            
            Long companyId = organisationManager.getCompanyIdByAuthId(authId)
                                               .getBody().getData();
            if (companyId == null) {
                throw new SurveyServiceException(ErrorType.COMPANY_ID_NOT_FOUND);
            }
            Survey survey = Survey.builder()
                                  .title(dto.getTitle())
                                  .description(dto.getDescription())
                                  .expirationDate(dto.getExpirationDate())
                                  .createdBy(authId)
                                  .state(EState.ACTIVE)
                                  .companyId(companyId)
                                  .build();
            
            surveyRepository.save(survey);
            
            dto.getQuestions().forEach(questionDto -> {
                Question question = Question.builder()
                                            .questionText(questionDto.getQuestionText())
                                            .questionType(questionDto.getQuestionType())
                                            .surveyId(survey.getId())
                                            .build();
                
                questionService.save(question);
                
                if (questionDto.getOptions() != null && !questionDto.getOptions().isEmpty()) {
                    List<String> optionIds = new ArrayList<>();
                    questionDto.getOptions().forEach(optionDto -> {
                        Option option = Option.builder()
                                              .questionId(question.getId())
                                              .optionText(optionDto.getOptionText())
                                              .build();
                        
                        optionService.save(option);
                        optionIds.add(option.getId());
                    });
                    question.setOptionIds(optionIds);
                    questionService.save(question);
                }
            });
            
            return true;
        } catch (SurveyServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Anket oluşturma hatası: ", e);
            throw new SurveyServiceException(ErrorType.SURVEY_CREATE_ERROR);
        }
    }
    
    public SurveyDetailDto getSurveyDetail(String token, String surveyId) {
        validateTokenAndCheckRole(token, "MEMBER", "STAFF");
        
        Survey survey = surveyRepository.findById(surveyId)
                                        .orElseThrow(() -> new SurveyServiceException(ErrorType.SURVEY_NOTFOUND));
        
        List<Question> questions = questionService.findAllBySurveyId(surveyId);
        System.out.println(surveyId);
        List<QuestionDetailDto> questionDetails = questions.stream()
                                                           .map(question -> {
                                                               List<Option> options = optionService.findAllByQuestionId(question.getId());
                                                               return QuestionDetailDto.builder()
                                                                                       .question(question)
                                                                                       .options(options)
                                                                                       .build();
                                                           })
                                                           .collect(Collectors.toList());
        
        return SurveyDetailDto.builder()
                              .survey(survey)
                              .questions(questionDetails)
                              .build();
    }
    
    public List<SurveyDetailDto> getAllSurveys(String token) {
        // Token'ı doğrula ve rolleri kontrol et
        Long authId = validateTokenAndCheckRole(token, "MEMBER", "STAFF");
        
        
        Long companyId = organisationManager.getCompanyIdByAuthId(authId)
                                            .getBody().getData();
        if (companyId == null) {
            throw new SurveyServiceException(ErrorType.COMPANY_ID_NOT_FOUND);
        }
        
        // Şirkete ait aktif anketleri getir
        return surveyRepository.findAllByCompanyIdAndState(companyId, EState.ACTIVE)
                .stream()
                .map(survey -> getSurveyDetail(token, survey.getId()))
                .collect(Collectors.toList());
    }
    

    
    @Transactional
    public Boolean deleteSurvey(String token, String surveyId) {
        // Anketi sadece MEMBER rolü ve anketi oluşturan kişi silebilir.
        Long authId = validateTokenAndCheckRole(token, "MEMBER");
        
        Survey survey = surveyRepository.findById(surveyId)
                                        .orElseThrow(() -> new SurveyServiceException(ErrorType.SURVEY_NOTFOUND));
        
        if (!survey.getCreatedBy().equals(authId)) {
            throw new SurveyServiceException(ErrorType.UNAUTHORIZED);
        }
        
        survey.setState(EState.PASSIVE);
        surveyRepository.save(survey);
        return true;
    }
    
    @Transactional
    public Boolean updateSurvey(String token, String surveyId, CreateSurveyRequestDto dto) {
        // Anketi sadece MEMBER rolü ve anketi oluşturan kişi güncelleyebilir.
        Long authId = validateTokenAndCheckRole(token, "MEMBER");
        
        Survey survey = surveyRepository.findById(surveyId)
                                        .orElseThrow(() -> new SurveyServiceException(ErrorType.SURVEY_NOTFOUND));
        
        if (!survey.getCreatedBy().equals(authId)) {
            throw new SurveyServiceException(ErrorType.UNAUTHORIZED);
        }
        
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setExpirationDate(dto.getExpirationDate());
        surveyRepository.save(survey);
        
        // Burada mevcut soruları pasif yapıyorum.
        questionService.findAllBySurveyId(surveyId)
                          .forEach(question -> {
                              question.setState(EState.PASSIVE);
                              questionService.save(question);
                          });
        
        // Burada da yeni soruları ekliyorum.
        dto.getQuestions().forEach(questionDto -> {
            Question question = Question.builder()
                                        .questionText(questionDto.getQuestionText())
                                        .questionType(questionDto.getQuestionType())
                                        .surveyId(survey.getId())
                                        .build();
            
            questionService.save(question);
            
            if (questionDto.getOptions() != null && !questionDto.getOptions().isEmpty()) {
                List<String> optionIds = new ArrayList<>();
                questionDto.getOptions().forEach(optionDto -> {
                    Option option = Option.builder()
                                          .questionId(question.getId())
                                          .optionText(optionDto.getOptionText())
                                          .build();
                    
                    optionService.save(option);
                    optionIds.add(option.getId());
                });
                question.setOptionIds(optionIds);
                questionService.save(question);
            }
        });
        
        return true;
    }
}
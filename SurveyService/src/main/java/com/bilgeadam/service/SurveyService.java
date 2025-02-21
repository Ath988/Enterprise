package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateSurveyRequestDto;
import com.bilgeadam.dto.request.OptionDto;
import com.bilgeadam.dto.request.QuestionDto;
import com.bilgeadam.dto.response.QuestionDetailDto;
import com.bilgeadam.dto.response.SurveyDetailDto;
import com.bilgeadam.entity.Option;
import com.bilgeadam.entity.Question;
import com.bilgeadam.entity.Survey;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.SurveyServiceException;
import com.bilgeadam.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionService questionService;
    private final OptionService optionService;

    public Survey createSurvey(CreateSurveyRequestDto dto) {
        Survey survey = Survey.builder()
                              .title(dto.getTitle())
                              .description(dto.getDescription())
                              .expirationDate(dto.getExpirationDate())
                              .createdBy(dto.getCreatedBy())
                              .build();
        
        survey = surveyRepository.save(survey);
        List<String> questionIds = createQuestionsForSurvey(dto.getQuestions(), survey.getId());
        survey.setQuestionIds(questionIds);
        
        return surveyRepository.save(survey);
    }

    private List<String> createQuestionsForSurvey(List<QuestionDto> questions, String surveyId) {
        return questions.stream()
                .map(questionDto -> {
                    Question question = Question.builder()
                                                .questionText(questionDto.getQuestionText())
                                                .questionType(questionDto.getQuestionType())
                                                .surveyId(surveyId)
                                                .build();
                    
                    question = questionService.save(question);
                    
                    if (questionDto.getOptions() != null && !questionDto.getOptions().isEmpty()) {
                        List<String> optionIds = createOptionsForQuestion(questionDto.getOptions(), question.getId());
                        question.setOptionIds(optionIds);
                        questionService.save(question);
                    }
                    
                    return question.getId();
                })
                .collect(Collectors.toList());
    }

    private List<String> createOptionsForQuestion(List<OptionDto> options, String questionId) {
        return options.stream()
                .map(optionDto -> {
                    Option option = Option.builder()
                                          .questionId(questionId)
                                          .optionText(optionDto.getOptionText())
                                          .build();
                    return optionService.save(option).getId();
                })
                .collect(Collectors.toList());
    }

    public List<Survey> getActiveSurveys() {
        return surveyRepository.findAllByState(EState.ACTIVE);
    }

    public SurveyDetailDto getSurveyWithDetails(String surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new SurveyServiceException(ErrorType.SURVEY_NOTFOUND));

        List<Question> questions = questionService.findAllByIdIn(survey.getQuestionIds());
        List<QuestionDetailDto> questionDtos = getQuestionDetailsWithOptions(questions);

        return SurveyDetailDto.builder()
                .survey(survey)
                .questions(questionDtos)
                .build();
    }

    private List<QuestionDetailDto> getQuestionDetailsWithOptions(List<Question> questions) {
        return questions.stream()
                .map(question -> {
                    List<Option> options = new ArrayList<>();
                    if (question.getOptionIds() != null) {
                        options = optionService.findAllByIdIn(question.getOptionIds());
                    }
                    return QuestionDetailDto.builder()
                            .question(question)
                            .options(options)
                            .build();
                })
                .collect(Collectors.toList());
    }
} 
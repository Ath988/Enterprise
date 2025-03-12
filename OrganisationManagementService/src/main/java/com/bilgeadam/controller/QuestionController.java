package com.bilgeadam.controller;

import com.bilgeadam.dto.request.QuestionRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.GetAllQuestionResponseDto;
import com.bilgeadam.dto.response.GetQuestionResponseDto;
import com.bilgeadam.entity.Question;
import com.bilgeadam.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(QUESTION)
@CrossOrigin("*")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping(GET_ALL_QUESTION)
    public ResponseEntity<BaseResponse<List<GetAllQuestionResponseDto>>> getAllQuestions(
            @RequestHeader(value = "Authorization", required = false) String token) {
        // Soruları al ve DTO'ya dönüştür
        List<GetAllQuestionResponseDto> responseDtos = questionService.getAllQuestions();

        // BaseResponse ile döndür
        return ResponseEntity.ok(BaseResponse.<List<GetAllQuestionResponseDto>>builder()
                .code(200)
                .data(responseDtos)
                .success(true)
                .build());
    }

    @GetMapping(GET_QUESTION)
    public ResponseEntity<BaseResponse<GetQuestionResponseDto>> getQuestionById(@RequestParam Long id) {
        // Soruyu ve cevapları al
        GetQuestionResponseDto responseDto = questionService.getQuestionById(id);

        // ResponseEntity ile BaseResponse içinde döndür
        return ResponseEntity.ok(BaseResponse.<GetQuestionResponseDto>builder()
                .code(200)
                .data(responseDto)
                .success(true)
                .build());
    }


    @PostMapping(ADD_QUESTION)
    public ResponseEntity<BaseResponse<Boolean>> createQuestion(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody QuestionRequestDto dto) {
        boolean isCreated = questionService.createQuestion(token, dto);
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                .code(200)
                .data(isCreated)
                .success(true)
                .build());
    }
}

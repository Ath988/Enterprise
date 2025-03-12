package com.bilgeadam.service;

import com.bilgeadam.dto.request.QuestionRequestDto;
import com.bilgeadam.dto.response.GetAllQuestionResponseDto;
import com.bilgeadam.dto.response.GetQuestionResponseDto;
import com.bilgeadam.dto.response.GetReplyResponseDto;
import com.bilgeadam.entity.Employee;
import com.bilgeadam.entity.Question;
import com.bilgeadam.entity.Reply;
import com.bilgeadam.repository.QuestionRepository;
import com.bilgeadam.utility.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final EmployeeService employeeService;
    private final ReplyService replyService;
    private final JwtManager jwtManager;

    public List<GetAllQuestionResponseDto> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();

        // Soruları DTO'ya dönüştür
        return questions.stream()
                .map(question -> {
                    // Employee bilgilerini almak için employeeId kullanıyoruz
                    String authorName = "Unknown";
                    if (question.getEmployeeId() != null) {
                        // EmployeeService üzerinden Employee'yi alıyoruz
                        authorName = employeeService.getEmployeeFullNameById(question.getEmployeeId());
                    }

                    // DTO'yu oluştur
                    return new GetAllQuestionResponseDto(
                            question.getId(),
                            question.getText(),
                            question.getECategory().toString(),
                            authorName,
                            question.getDate()
                    );
                })
                .collect(Collectors.toList());
    }

    public GetQuestionResponseDto getQuestionById(Long id) {
        // Soruyu al
        Question question = questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));

        // Sorunun yazarının ismini al
        String authorName = employeeService.getEmployeeFullNameById(question.getEmployeeId());

        // Cevapları al
        List<Reply> replies = replyService.findByQuestionId(id);
        List<GetReplyResponseDto> replyDtos = replies.stream()
                .map(reply -> new GetReplyResponseDto(
                        reply.getText(),
                        employeeService.getEmployeeFullNameById(reply.getEmployeeId()), // Cevap yazarı
                        reply.getDate()
                ))
                .collect(Collectors.toList());

        // Soruyu ve cevapları DTO'ya dönüştür
        return new GetQuestionResponseDto(
                question.getId(),
                question.getText(),
                question.getECategory().name(),
                authorName,
                question.getDate(),
                replyDtos
        );
    }

    public boolean createQuestion(String token, QuestionRequestDto dto) {
        // Token'dan Employee ID al

        Long employeeId = employeeService.getEmployeeIdFromToken(token);
        if (employeeId == null) {
            throw new RuntimeException("Unauthorized: Invalid token!");
        }
        // Yeni Question nesnesi oluştur
        Question question = Question.builder()
                .text(dto.text())
                .eCategory(dto.eCategory())
                .employeeId(employeeId) // Token'dan alınan kullanıcı ID
                .date(LocalDateTime.now()) // Şu anki tarih
                .build();

        // Veritabanına kaydet
        questionRepository.save(question);

        // Kaydetme işlemi başarılıysa true döndür
        return true;
    }
}

package com.bilgeadam.service;

import com.bilgeadam.dto.request.VoteRequestDto;
import com.bilgeadam.dto.request.VoteSummaryDto;
import com.bilgeadam.entity.Question;
import com.bilgeadam.entity.Vote;
import com.bilgeadam.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final EmployeeService employeeService;
    private final QuestionService questionService;
    public Boolean addVoteDislike;


    public Boolean addVoteLike(VoteRequestDto dto) {
       Optional<Question> question = questionService.findById(dto.questionId());
       if (question.isEmpty()){
           throw new RuntimeException("question not found");
       }
       Vote vote = Vote.builder()
               .questionId(dto.questionId())
               .likeCount(dto.Like())
               .build();

       voteRepository.save(vote);
       return true;
    }

    public Boolean addVoteDislike(VoteRequestDto dto) {
        Optional<Question> question = questionService.findById(dto.questionId());
        if (question.isEmpty()){
            throw new RuntimeException("question not found");
        }
        Vote vote = Vote.builder()
                .questionId(dto.questionId())
                .disLikeCount(dto.Like())
                .build();

        voteRepository.save(vote);
        return true;
    }

    public List<VoteSummaryDto> getAllVoteSummaries() {
        return voteRepository.getVoteSummaryByQuestion();
    }

    public VoteSummaryDto getVoteSummaryForQuestion(Long questionId) {
        return voteRepository.getVoteSummaryForQuestion(questionId);
    }



}

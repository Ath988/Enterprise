package com.bilgeadam.repository;

import com.bilgeadam.dto.request.VoteSummaryDto;
import com.bilgeadam.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT new com.bilgeadam.dto.request.VoteSummaryDto(v.questionId, SUM(v.likeCount), SUM(v.disLikeCount)) " +
            "FROM Vote v GROUP BY v.questionId")
    List<VoteSummaryDto> getVoteSummaryByQuestion();

    @Query("SELECT new com.bilgeadam.dto.request.VoteSummaryDto(v.questionId, SUM(v.likeCount), SUM(v.disLikeCount)) " +
            "FROM Vote v WHERE v.questionId = :questionId GROUP BY v.questionId")
    VoteSummaryDto getVoteSummaryForQuestion(Long questionId);
}

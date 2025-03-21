package com.bilgeadam.controller;

import com.bilgeadam.dto.request.VoteRequestDto;
import com.bilgeadam.dto.request.VoteSummaryDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Vote;
import com.bilgeadam.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(VOTE)
@CrossOrigin("*")
public class VoteController {
    private final VoteService voteService;

   @PostMapping(ADD_VOTE_LIKE)
    public ResponseEntity<BaseResponse<Boolean>> addVoteLike(@RequestBody VoteRequestDto dto){
       return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                       .data(voteService.addVoteLike(dto))
                       .message("vote başarıyla oluşturuldu")
                       .code(200)
                       .success(true)
               .build());
   }
   @PostMapping(ADD_VOTE_DISLIKE)
    public ResponseEntity<BaseResponse<Boolean>> addVoteDislike(@RequestBody VoteRequestDto dto){
       return ResponseEntity.ok(BaseResponse.<Boolean>builder()
               .data(voteService.addVoteDislike(dto))
               .message("dis vote başarıyla oluşturuldu")
               .code(200)
               .success(true)
               .build());
   }

    // Tüm question'lara ait toplam like & dislike sayısını döndürür
    @GetMapping("/summary")
    public ResponseEntity<List<VoteSummaryDto>> getAllVoteSummaries() {
        return ResponseEntity.ok(voteService.getAllVoteSummaries());
    }

    // Belirli bir questionId için toplam like & dislike sayısını döndürür
    @GetMapping("/summary/{questionId}")
    public ResponseEntity<VoteSummaryDto> getVoteSummaryForQuestion(@PathVariable Long questionId) {
        return ResponseEntity.ok(voteService.getVoteSummaryForQuestion(questionId));
    }

}
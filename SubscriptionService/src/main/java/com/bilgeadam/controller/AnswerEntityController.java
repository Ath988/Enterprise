package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddAnswerRequestDto;
import com.bilgeadam.dto.request.UpdateAnswerDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.AnswerEntity;
import com.bilgeadam.service.AnswerEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ANSWER)
@CrossOrigin("*")
public class AnswerEntityController {
	private final AnswerEntityService answerEntityService;
	
	@GetMapping(GET_ALL_ANSWERS)
	public ResponseEntity<BaseResponse<List<AnswerEntity>>> getAllAnswers(){
		return ResponseEntity.ok(BaseResponse.<List<AnswerEntity>>builder()
				                         .code(200)
				                         .data(answerEntityService.getAllAnswers())
				                         .message("cevaplar başarıyla getirildi")
				                         .success(true)
		                                     .build());
	}
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@PutMapping(UPDATE_ANSWER)
	public ResponseEntity<BaseResponse<AnswerEntity>> updateAnswer(@RequestBody UpdateAnswerDto dto){
		AnswerEntity update = answerEntityService.updateAnswer(dto);
		
		return ResponseEntity.ok(BaseResponse.<AnswerEntity>builder()
				                         .success(true)
				                         .code(200)
				                         .message("cevap başarıyla guncellendi")
				                         .data(update)
		                                     .build());
	}
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@PostMapping(CREATE_ANSWER)
	public ResponseEntity<BaseResponse<Boolean>> createAnswer(@RequestBody AddAnswerRequestDto dto){
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .data(answerEntityService.createAnswer(dto))
				                         .message("cevap başarıyla oluşturuldu")
				                         .code(200)
				                         .success(true)
		                                     .build());
		
	}
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@DeleteMapping(CANCEL_ANSWER)
	public ResponseEntity<BaseResponse<Boolean>> deleteAnswer(Long id){
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .success(true)
				                         .data(answerEntityService.deleteAnswer(id))
				                         .message("cevap başarıyla silindi")
				                         .code(200)
		                                     .build());
	}
}
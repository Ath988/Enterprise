package com.bilgeadam.controller;

import com.bilgeadam.dto.request.ReplyRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Reply;
import com.bilgeadam.service.EmployeeService;
import com.bilgeadam.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(REPLY)
@CrossOrigin("*")
public class ReplyController {
    private final ReplyService replyService;
    private final EmployeeService employeeService;

    @PostMapping(ADD_REPLY)
    public ResponseEntity<BaseResponse<Boolean>> createReply(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ReplyRequestDto dto) {

        // Token'dan kullanıcı kimliğini al
        Long employeeId = employeeService.getEmployeeIdFromToken(token);
        if (employeeId == null) {
            return ResponseEntity.status(401).body(BaseResponse.<Boolean>builder()
                    .code(401)
                    .data(false)
                    .success(false)
                    .message("Unauthorized: Invalid or missing token.")
                    .build());
        }

        // Cevap oluşturma işlemi
        boolean isCreated = replyService.createReply(employeeId, dto);

        // Cevap başarıyla oluşturulmuşsa başarılı, aksi takdirde başarısız durumu döndür
        if (isCreated) {
            return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                    .code(200)
                    .data(true)
                    .success(true)
                    .message("Reply created successfully.")
                    .build());
        } else {
            return ResponseEntity.status(500).body(BaseResponse.<Boolean>builder()
                    .code(500)
                    .data(false)
                    .success(false)
                    .message("Failed to create reply.")
                    .build());
        }
    }
    @GetMapping(GET_ALL_REPLY)
    public ResponseEntity<BaseResponse<List<Reply>>> getAllReplies() {
        return ResponseEntity.ok(BaseResponse.<List<Reply>>builder()
                .message("Tüm yanıtlar başarıyla getirildi.")
                .data(replyService.getAllReplies())
                .build());
    }
}

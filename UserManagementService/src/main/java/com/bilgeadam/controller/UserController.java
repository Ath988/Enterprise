package com.bilgeadam.controller;

import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.User;
import com.bilgeadam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
@CrossOrigin("*")

public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> testCreate(@RequestParam String kullanici){
        return ResponseEntity.ok(BaseResponse.<Boolean>builder()
                        .success(userService.createCompanyOwner(kullanici))
                .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<User>>> testGetAll(){
        return ResponseEntity.ok(BaseResponse.<List<User>>builder()
                        .data(userService.findAllTest())
                .build());
    }
}

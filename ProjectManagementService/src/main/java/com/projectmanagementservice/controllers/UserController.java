package com.projectmanagementservice.controllers;

import com.projectmanagementservice.dto.request.*;
import com.projectmanagementservice.entities.Task;
import com.projectmanagementservice.entities.User;
import com.projectmanagementservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.projectmanagementservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT+USER)
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController
{
    private final UserService userService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody UserSaveRequestDTO dto){

        return ResponseEntity.ok(userService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(userService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody UserUpdateRequestDTO dto){

        return ResponseEntity.ok(userService.update(dto));
    }

    @PostMapping(FIND_ALL)
    public ResponseEntity<List<User>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(userService.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByNameAsc(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<User> findByIdAndAuthId(Long id){

        return ResponseEntity.ok(userService.findByIdAndAuthId(id));
    }


}

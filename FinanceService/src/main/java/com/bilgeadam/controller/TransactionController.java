package com.bilgeadam.controller;

import com.bilgeadam.dto.request.TransactionSaveRequestDTO;
import com.bilgeadam.dto.request.TransactionUpdateRequestDTO;
import com.bilgeadam.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(TRANSACTION)
@RequiredArgsConstructor
@CrossOrigin("*")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody TransactionSaveRequestDTO dto){

        return ResponseEntity.ok(transactionService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(transactionService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody TransactionUpdateRequestDTO dto){

        return ResponseEntity.ok(transactionService.update(dto));
    }



}

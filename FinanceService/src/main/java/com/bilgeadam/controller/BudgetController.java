package com.bilgeadam.controller;

import com.bilgeadam.dto.request.BudgetSaveRequestDTO;
import com.bilgeadam.dto.request.BudgetUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.dto.response.BudgetByDepartmentResponseDTO;
import com.bilgeadam.dto.response.ResponseDTO;
import com.bilgeadam.entity.Budget;
import com.bilgeadam.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(BUDGET)
@CrossOrigin("*")
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping(SAVE_BUDGET)
    public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody BudgetSaveRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(budgetService.save(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @PutMapping(UPDATE_BUDGET)
    public ResponseEntity<ResponseDTO<Boolean>> update(@RequestBody BudgetUpdateRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(budgetService.update(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @DeleteMapping(DELETE_BUDGET)
    public ResponseEntity<ResponseDTO<Boolean>> delete(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Boolean>builder()
                .data(budgetService.delete(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(GET_ALL_BUDGETS)
    public ResponseEntity<List<Budget>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(budgetService.findAll(dto));
    }

    @PostMapping(GET_BUDGET_BY_ID)
    public ResponseEntity<ResponseDTO<Budget>> findById(Long id) {
        return ResponseEntity.ok(ResponseDTO
                .<Budget>builder()
                .data(budgetService.findById(id))
                .message("Success")
                .code(200)
                .build());
    }

    @PostMapping(FIND_ALL_BY_DEPARTMENT_NAME)
    public ResponseEntity<ResponseDTO<List<BudgetByDepartmentResponseDTO>>> findAllByDepartmentId(String departmentName) {
        return ResponseEntity.ok(ResponseDTO
                .<List<BudgetByDepartmentResponseDTO>>builder()
                .data(budgetService.findAllByDepartmentName(departmentName))
                .message("Success")
                .code(200)
                .build());
    }

}
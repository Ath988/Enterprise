package com.inventoryservice.controllers;


import com.inventoryservice.dto.request.*;
import com.inventoryservice.dto.response.BuyOrderResponseDTO;
import com.inventoryservice.dto.response.StockMovementResponseDTO;
import com.inventoryservice.entities.BuyOrder;
import com.inventoryservice.entities.StockMovement;
import com.inventoryservice.services.BuyOrderService;
import com.inventoryservice.services.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inventoryservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + STOCKMOVEMENT)
@RequiredArgsConstructor
@CrossOrigin("*")
public class StockMovementController
{
    private final StockMovementService stockMovementService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody StockMovementSaveRequestDTO dto){

        return ResponseEntity.ok(stockMovementService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(stockMovementService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody StockMovementUpdateRequestDTO dto){

        return ResponseEntity.ok(stockMovementService.update(dto));
    }

    @PostMapping(FIND_ALL)

    public ResponseEntity<List<StockMovementResponseDTO>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(stockMovementService.findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<StockMovement> findById(Long id){

        return ResponseEntity.ok(stockMovementService.findByIdAndAuthId(id));
    }



}

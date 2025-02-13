package com.inventoryservice.controllers;


import com.inventoryservice.dto.request.*;
import com.inventoryservice.dto.response.BuyOrderResponseDTO;
import com.inventoryservice.entities.BuyOrder;
import com.inventoryservice.entities.Product;
import com.inventoryservice.services.BuyOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inventoryservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + BUYORDER)
@RequiredArgsConstructor
@CrossOrigin("*")
public class BuyOrderController
{
    private final BuyOrderService buyOrderService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody BuyOrderSaveRequestDTO dto){

        return ResponseEntity.ok(buyOrderService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(buyOrderService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody BuyOrderUpdateRequestDTO dto){

        return ResponseEntity.ok(buyOrderService.update(dto));
    }

    @PostMapping(FIND_ALL)

    public ResponseEntity<List<BuyOrderResponseDTO>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(buyOrderService.findAllByProduct_NameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByProduct_NameAsc(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<BuyOrder> findById(Long id){

        return ResponseEntity.ok(buyOrderService.findByIdAndAuthId(id));
    }



}

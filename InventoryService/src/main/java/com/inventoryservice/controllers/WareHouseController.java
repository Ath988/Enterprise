package com.inventoryservice.controllers;


import com.inventoryservice.dto.request.*;
import com.inventoryservice.entities.Supplier;
import com.inventoryservice.entities.WareHouse;
import com.inventoryservice.services.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inventoryservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + WAREHOUSE)
@RequiredArgsConstructor
@CrossOrigin("*")
public class WareHouseController
{
    private final WareHouseService wareHouseService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody WareHouseSaveRequestDTO dto){

        return ResponseEntity.ok(wareHouseService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(wareHouseService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody WareHouseUpdateRequestDTO dto){

        return ResponseEntity.ok(wareHouseService.update(dto));
    }

    @PostMapping(FIND_ALL)

    public ResponseEntity<List<WareHouse>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(wareHouseService.findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<WareHouse> findById(Long id){

        return ResponseEntity.ok(wareHouseService.findByIdAndAuthId(id));
    }



}

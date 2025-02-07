package com.inventoryservice.controllers;


import com.inventoryservice.dto.request.*;
import com.inventoryservice.entities.BuyOrder;
import com.inventoryservice.entities.Supplier;
import com.inventoryservice.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inventoryservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + SUPPLIER)
@RequiredArgsConstructor
@CrossOrigin("*")
public class SupplierController
{
    private final SupplierService supplierService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody SupplierSaveRequestDTO dto){

        return ResponseEntity.ok(supplierService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(supplierService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody SupplierUpdateRequestDTO dto){

        return ResponseEntity.ok(supplierService.update(dto));
    }

    @PostMapping(FIND_ALL)

    public ResponseEntity<List<Supplier>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(supplierService.findAllByNameContainingIgnoreCaseAndAuthIdAndStatusIsNotOrderByNameAsc(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<Supplier> findById(Long id){

        return ResponseEntity.ok(supplierService.findByIdAndAuthId(id));
    }



}

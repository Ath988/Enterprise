package com.inventoryservice.controllers;


import com.inventoryservice.dto.request.PageRequestDTO;
import com.inventoryservice.dto.request.ProductSaveRequestDTO;
import com.inventoryservice.dto.request.ProductUpdateRequestDTO;
import com.inventoryservice.dto.response.ProductResponseDTO;
import com.inventoryservice.entities.Product;
import com.inventoryservice.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inventoryservice.constants.Endpoints.*;

@RestController
@RequestMapping(ROOT + PRODUCT)
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController
{
    private final ProductService productService;

    @PostMapping(SAVE)
    public ResponseEntity<Boolean> save(@RequestBody ProductSaveRequestDTO dto){

        return ResponseEntity.ok(productService.save(dto));
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(productService.delete(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody ProductUpdateRequestDTO dto){

        return ResponseEntity.ok(productService.update(dto));
    }

    @PostMapping(FIND_ALL)

    public ResponseEntity<List<ProductResponseDTO>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(productService.findAllByNameContainingIgnoreCaseAndStatusIsNotAndAuthIdOrderByName(dto));
    }

    @PostMapping(FIND_BY_ID)
    public ResponseEntity<Product> findById(Long id){

        return ResponseEntity.ok(productService.findByIdAndAuthId(id));
    }



}

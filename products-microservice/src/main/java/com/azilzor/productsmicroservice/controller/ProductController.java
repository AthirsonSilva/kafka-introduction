package com.azilzor.productsmicroservice.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azilzor.productsmicroservice.dto.CreateProductDto;
import com.azilzor.productsmicroservice.exception.ErrorMessage;
import com.azilzor.productsmicroservice.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductDto createProductDto) {
        var response = "";
        try {
            response = productService.createProduct(createProductDto);
        } catch (Exception e) {
            log.error("Error occurred while creating product", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(
                            LocalDateTime.now(),
                            "Error occurred while creating product",
                            e.getMessage()).toString());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}

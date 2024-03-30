package com.azilzor.productsmicroservice.service;

import com.azilzor.productsmicroservice.dto.CreateProductDto;

public interface ProductService {
    String createProduct(CreateProductDto createProductDto);
}

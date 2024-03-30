package com.azilzor.productsmicroservice.service.impl;

import com.azilzor.productsmicroservice.dto.CreateProductDto;
import com.azilzor.productsmicroservice.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public String createProduct(CreateProductDto createProductDto) {
        return "Product created successfully";
    }
}

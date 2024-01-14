package com.example.ProductService.Controller;

import com.example.ProductService.DTO.ProductRequest;
import com.example.ProductService.DTO.ProductResponse;
import com.example.ProductService.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;
        @PostMapping(value = "/create")
        @ResponseStatus(HttpStatus.CREATED)
        public void createProduct(@RequestBody ProductRequest productRequest){
            productService.createProduct(productRequest);
        }
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public List<ProductResponse> getAllProducts(){
         return productService.getAllProducts();
        }

}

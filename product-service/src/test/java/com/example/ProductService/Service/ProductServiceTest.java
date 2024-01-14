package com.example.ProductService.Service;

import com.example.ProductService.DTO.ProductRequest;
import com.example.ProductService.DTO.ProductResponse;
import com.example.ProductService.Model.Product;
import com.example.ProductService.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension to initialize mocks
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    ProductService productService;
    @BeforeEach
    void setUp(){
        this.productService = new ProductService(productRepository);
    }
    @Test
    void shouldInvokeCreateService(){
        ProductRequest productRequest = getProductRequest();


        // Create a new Product object
        when(productRepository.save(any(Product.class))).thenReturn(getProduct(productRequest)); // Mock the save method to return the newProduct
        //ACT
        Product foundResult = productService.createProduct(productRequest);
        //Asserstions
        assertThat(foundResult).isEqualTo(getProduct(productRequest));
        assertEquals("ABC",foundResult.getName());
        assertEquals("its costly",foundResult.getDescription());
    }

    @Test
    void shouldInvokeGetService(){
        when(productRepository.findAll()).thenReturn(getProductList());
        List<ProductResponse> productList = productService.getAllProducts();
        assertEquals("ABC",productList.get(0).getName());
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder().name("ABC").description("its costly").price(BigDecimal.valueOf(1232000.232))
                .build();
    }


    private Product getProduct(ProductRequest productRequest){
        return Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .build();
    }
    private List<Product> getProductList(){
        List<Product> products = new ArrayList<>();
        products.add(new Product("1","ABC","costly",BigDecimal.valueOf(123.23)));
        products.add(new Product("2","DFG","its damn ",BigDecimal.valueOf(223.23)));
        return products;
    }

}

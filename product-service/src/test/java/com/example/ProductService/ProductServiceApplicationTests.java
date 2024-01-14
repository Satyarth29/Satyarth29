package com.example.ProductService;

import com.example.ProductService.DTO.ProductRequest;
import com.example.ProductService.Model.Product;
import com.example.ProductService.Repository.ProductRepository;
import com.example.ProductService.Service.ProductService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(properties = {
		"eureka.client.enabled=false"})
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Container
	public static MongoDBContainer container = new MongoDBContainer(DockerImageName.parse("mongo:5"));

	@DynamicPropertySource
	static void mongoDbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
	}//the test will download the static add this container and fetch the uri

	//  mongodb container


	@Test
	void shouldCreateProductRequest() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestAsString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestAsString).characterEncoding("utf-8"))
				.andExpect(status().isCreated()).andDo(print());
	}
	@Test
	void shouldGetProductRequest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product").
				contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	//creating an object
	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("caleb")
				.price(BigDecimal.valueOf(12000))
				.description("yeah")
				.build();
	}
}

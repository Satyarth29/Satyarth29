
package com.example.OrderService;

import com.example.OrderService.Controller.OrderController;
import com.example.OrderService.Repository.OrderRepository;
import com.example.OrderService.config.EurekaContainerConfig;
import com.example.OrderService.config.WebClientConfig;
import com.example.OrderService.config.WireMockConfig;
import com.example.OrderService.dto.InventoryResponse;
import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItems;
import com.example.OrderService.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.netflix.discovery.shared.Application;
import lombok.AllArgsConstructor;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/*
@SpringBootTest(properties = {
		"eureka.client.enabled=false"})
@Testcontainers
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebAppConfiguration("classpath:META-INF/web-resources")
class OrderServiceApplicationTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	OrderRepository orderRepository;
	@Mock
	InventoryResponse inventoryResponse;


	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0-debian"));


	@DynamicPropertySource
	static void mySqlDbProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
		registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
		registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
		registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
	}



		@Test
		void shouldInvokeCreateController() throws Exception {

				OrderRequest orderRequest = OrderRequest.builder().orderLineItemDtoList( getOrderLineitems()).build() ;

				// Perform a POST request to the /api/Order/create endpoint with the JSON body of the OrderRequest
				mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(orderRequest)))
						.andExpect(status().isCreated()).andDo(print());

			}

	private List<OrderLineItemDto> getOrderLineitems() {
				List<OrderLineItemDto> list = new ArrayList<>();
				list.add(new OrderLineItemDto("102",2.0,12));
				list.add(new OrderLineItemDto("103",3.0,13));
				list.add(new OrderLineItemDto("104",4.0,14));
				return list;
			}


		}


*/

import com.example.OrderService.Repository.OrderRepository;
import com.example.OrderService.config.WebClientConfig;
import com.example.OrderService.dto.InventoryResponse;
import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItems;
import com.example.OrderService.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


// Port for WireMock, you can change it if needed


@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
/*@ContextConfiguration(classes = { WireMockConfig.class })*/
@Testcontainers
@AutoConfigureWireMock(port=9999)
class OrderServiceApplicationTests {
@Autowired
WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WireMockServer mockInventoryResponse;
    @Autowired
    OrderController orderController;




    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0-debian"));
    @BeforeAll
    static void beforeAll(){
    mySQLContainer.start();
}
   @AfterAll
    static void afterAll(){mySQLContainer.stop();}
    @DynamicPropertySource
    static void mySqlDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
    }



 /*     @Autowired
    OrderController orderController;
    @BeforeEach
    void setUp() throws IOException {
        InventoryMocks.setupMockBooksResponse(mockBooksService);
    }


    @Test
    public void whenGetBooks_thenBooksShouldBeReturned() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemDtoList(Collections.singletonList(
                new OrderLineItemDto("sku-001", 100.0, 1)
        ));

        when()
        assertFalse(orderController.placeOrder(orderRequest).isEmpty());
    }


     @Test
    public void whenGetBooks_thenTheCorrectBooksShouldBeReturned() {
        assertTrue(booksClient.getBooks()
                .containsAll(asList(
                        new Book("Dune", "Frank Herbert"),
                        new Book("Foundation", "Isaac Asimov"))));
    }
*/
   @Test
    void placeOrder_Success() throws Exception {
       OrderRequest orderRequest = new OrderRequest();
       orderRequest.setOrderLineItemDtoList(Collections.singletonList(
               new OrderLineItemDto("sku-001", 100.0, 1)
       ));
      // Mock the inventory-service response using WireMock
        mockInventoryResponse.stubFor(post(urlEqualTo("${inventory-service.url}/api/inventory"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(201)
                        .withBody(objectMapper.writeValueAsString(new InventoryResponse[] {
                                new InventoryResponse("sku-001", true),
                                new InventoryResponse("sku-002", true)
                        }))));


    webTestClient.post()
                .uri("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(orderRequest).exchange().expectStatus().isCreated();



        // Perform assertions on the response

        // Additional assertions

        orderController.placeOrder(orderRequest);
        // Mock the orderRepository behavior

        // Perform the order placement

        // Assert the result
        // Add assertions here based on the expected behavior

    }
}

    // You can write more test cases for various scenarios


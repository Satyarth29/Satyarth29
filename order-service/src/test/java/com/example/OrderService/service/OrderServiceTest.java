package com.example.OrderService.service;

import com.example.OrderService.Repository.OrderRepository;
import com.example.OrderService.dto.InventoryResponse;
import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItems;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
//we need to enable Mockito annotations to use this annotation.
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private InventoryResponse inventoryResponse;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private WebClient webClientBuilder;


    OrderService orderService;
    @BeforeEach
    void setUp() {
        this.orderService = new OrderService(webClientBuilder,orderRepository);
    }

    private MockWebServer mockWebServer;

/*
    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockitoAnnotations.initMocks(this);

        WebClient webClientMock = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        Mockito.when(webClientBuilder.build()).thenReturn(webClientMock);
    }

    @AfterEach
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
*/


    @Test
    public void testPlaceOrderWhenAllProductsInStockThenOrderSaved() {
        // Create a sample order request
        String expectedURI = "http://inventory-service/api/inventory";
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemDtoList(List.of(new OrderLineItemDto("sku1", 10.0, 2)));

        // Create a sample order
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(List.of(new OrderLineItems(1L, "sku1", 10.0, 2)));
 /*       InventoryResponse inventoryResponse1 = new InventoryResponse("sku1",true);


        String mockedResponse = "[{\"item\":\"item1\",\"quantity\":10},{\"item\":\"item2\",\"quantity\":20}]";
        mockWebServer.enqueue(new MockResponse().setBody(mockedResponse));
    Create sample data for testing
         Mock the behavior of the webClientBuilder.build().post() method
        when(webClientBuilder.build().post().uri(anyString()).body(any()).retrieve().bodyToMono(InventoryResponse[].class).block())
                .thenReturn(new InventoryResponse[]{new InventoryResponse("sku1", true)});


        when(webClientBuilder.build()).thenReturn(WebClient.create().mutate().baseUrl("http://example.com").build());*/
        // Mocking WebClient behavior
        // Mocking WebClient behavior
        /*WebClient webClientMock = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestHeadersUriSpecMock.uri(eq(expectedURI))).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(eq(orderRequest))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(InventoryResponse.class)).thenReturn(Mono.just(inventoryResponse));
        when(orderRepository.save(any())).thenReturn(order); // Mock saving the order
        // Call the placeOrder method
        orderService.placeOrder(orderRequest);*/
        // Mocking WebClient behavior
        WebClient.RequestBodyUriSpec requestBodyUriSpecMock = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        // Configure webClientBuilder to return the mock WebClient
        WebClient webClientMock = mock(WebClient.class);


        // Configure mock WebClient behavior
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.bodyValue(eq(orderRequest))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);


        // Configure responseSpecMock to return a predefined response
        when(responseSpecMock.bodyToMono(InventoryResponse[].class))
                .thenReturn(Mono.just(new InventoryResponse[]{new InventoryResponse("SKU123", true)})); // adjust accordingly
        assertDoesNotThrow(() -> orderService.placeOrder(orderRequest));

        // Verifying that save method is called
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    public void testPlaceOrderWhenSomeProductsOutOfStockThenOrderNotSaved() {
        // Create a sample order request
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemDtoList(List.of(new OrderLineItemDto("sku1", 10.0, 2)));
        when(orderRepository.save(any())).thenReturn(new Order());
        // Mock the behavior of the webClientBuilder.build().post() method
        when(webClientBuilder
                .post().uri(anyString())
                .body(any())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block())
                .thenReturn(new InventoryResponse[]{new InventoryResponse("sku1", false)});

        // Call the placeOrder method
        orderService.placeOrder(orderRequest);

        // Verify that the orderRepository.save method was not called
        verify(orderRepository, times(0)).save(any());
    }

    @Test
    public void testPlaceOrderWhenSomeProductsNotRegisteredThenOrderNotSaved() {
        // Create a sample order request
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemDtoList(List.of(new OrderLineItemDto("sku1", 10.0, 2)));
        when(orderRepository.save(any())).thenReturn(new Order());
        // Mock the behavior of the webClientBuilder.build().post() method
        when(webClientBuilder.post().uri(anyString()).body(any()).retrieve().bodyToMono(InventoryResponse[].class).block())
                .thenReturn(new InventoryResponse[]{});

        // Call the placeOrder method
        orderService.placeOrder(orderRequest);

        // Verify that the orderRepository.save method was not called
        verify(orderRepository, times(0)).save(any());
    }
}
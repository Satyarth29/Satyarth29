package com.example.OrderService.Controller;

import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.OrderLineItems;
import com.example.OrderService.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldInvokeCreateController() throws Exception {
    OrderRequest orderRequest = OrderRequest.builder().orderLineItemDtoList(getOrderLineitems()).build() ;

        // Perform a POST request to the /api/Order/create endpoint with the JSON body of the OrderRequest
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated());
    }


    @Test
    void shouldInvokeNullCreateController() throws Exception {

        OrderRequest orderRequest = OrderRequest.builder().build() ;

        // Perform a POST request to the /api/Order/create endpoint with the JSON body of the OrderRequest
        mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }


    private List<OrderLineItemDto>  getOrderLineitems() {
        List<OrderLineItemDto> list = new ArrayList<>();
    list.add(new OrderLineItemDto("102",2.0,12));
    list.add(new OrderLineItemDto("103",3.0,13));
    list.add(new OrderLineItemDto("104",4.0,14));
    return list;
    }

}
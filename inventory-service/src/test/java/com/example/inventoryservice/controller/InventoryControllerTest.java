package com.example.inventoryservice.controller;

import com.example.inventoryservice.Repository.InventoryRepo;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.OrderLineItemDto;
import com.example.inventoryservice.model.OrderRequest;
import com.example.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    InventoryRepo inventoryRepo;
    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testIsInStockWhenItemsInStockThenReturnInventoryResponseList() throws Exception {
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto("skuCode1", 100.0, 1);
        OrderRequest orderRequest = new OrderRequest(Arrays.asList(orderLineItemDto));

        InventoryResponse inventoryResponse = InventoryResponse.builder()
                .skuCode("skuCode1")
                .isInStock(true)
                .build();

        when(inventoryService.isInStock(any(OrderRequest.class))).thenReturn(Arrays.asList(inventoryResponse));

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(inventoryResponse))));
    }

    @Test
    public void testIsInStockWhenItemsNotInStockThenReturnEmptyList() throws Exception {
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
        OrderRequest orderRequest = new OrderRequest(Arrays.asList(orderLineItemDto));

        when(inventoryService.isInStock(any(OrderRequest.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));
    }

    @Test
    public void testIsInStockWhenItemsNullThenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }
}

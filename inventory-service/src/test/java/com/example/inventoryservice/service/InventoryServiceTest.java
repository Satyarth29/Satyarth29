package com.example.inventoryservice.service;

/*import com.example.OrderService.dto.*;
import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.service.InventoryService;*/
import com.example.inventoryservice.Repository.InventoryRepo;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.OrderLineItemDto;
import com.example.inventoryservice.model.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    InventoryService inventoryService;
    //Mocking is the act of removing external dependencies from a unit test in order to create a controlled environment around it. Typically, we mock all other classes that interact with the class that we want to test. Common targets for mocking are:
    //Database connections,
    //Web services,
    //Classes that are slow,
    //Classes with side effects, and
    //Classes with non-deterministic behavior.
    @Mock
    InventoryRepo inventoryrepo;

    @BeforeEach
    void setUp(){
        this.inventoryService = new InventoryService(inventoryrepo);
    }

   @Test
    public void testCheckInventory() {
        //GIVEN
       OrderRequest orderRequest = new OrderRequest();
        List<OrderLineItemDto> orderLineItems = Arrays.asList(
                new OrderLineItemDto("SKU001", 10.0, 2),
                new OrderLineItemDto("SKU002", 20.0, 3)
        );
       orderRequest.setOrderLineItemDtoList(orderLineItems);
       List<Inventory> inventoryList = new ArrayList<>();
       inventoryList.add(new Inventory(1L,"SKU001", 76));
       inventoryList.add(new Inventory(2L,"SKU002", 0));
       inventoryList.add(new Inventory(3L,"SKU003", 10));

       //this line uses mockito to define how the mock inventory repo responds when its findby skucode method is called this method is called stubbing
       when(inventoryrepo.findBySkuCodeIn(anyList())).thenReturn(inventoryList);

        //WHEN
        List<InventoryResponse> inventoryResponses = inventoryService.isInStock(orderRequest);
        // THEN
        assertEquals(3, inventoryResponses.size());
        assertEquals("SKU001", inventoryResponses.get(0).getSkuCode());
        assertTrue(inventoryResponses.get(0).isInStock());
        assertEquals("SKU002", inventoryResponses.get(1).getSkuCode());
        assertFalse(inventoryResponses.get(1).isInStock());
    }/*
this is the Behaviorial driven design from the given set of inputs ,when u do an action X,then expect Y
    public void testIsInStock() {
        OrderRequest orderRequest = new OrderRequest();
        List<OrderLineItemDto> orderLineItemDtoList = new ArrayList<>();
        orderLineItemDtoList.add(new OrderLineItemDto("SKU001", 2));
        orderLineItemDtoList.add(new OrderLineItemDto("SKU002", 1));
        orderRequest.setOrderLineItemDtoList(orderLineItemDtoList);



        List<InventoryResponse> inventoryResponses = inventoryService.isInStock(orderRequest);

        assertEquals(2, inventoryResponses.size());
        assertTrue(inventoryResponses.get(0).isInStock());
        assertFalse(inventoryResponses.get(1).isInStock());
    }*/
}
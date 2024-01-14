package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.Repository.InventoryRepo;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.OrderLineItemDto;
import com.example.inventoryservice.model.OrderLineItems;
import com.example.inventoryservice.model.OrderRequest;
import com.example.inventoryservice.service.InventoryService;
import jakarta.persistence.criteria.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Slf4j
public class InventoryController {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    InventoryRepo inventoryRepo;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestBody OrderRequest orderLineItems/*@RequestParamList<String> skuCode,*/){
        log.info("Received inventory check request for skuCode: {}",orderLineItems.getOrderLineItemDtoList().stream().map(OrderLineItemDto::getSkuCode).toList()/*, skuCode*/);
        return inventoryService.isInStock(/*skuCode,*/ orderLineItems);
    }

    //custom-made by for testing purpose only delete later
    /*@PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Inventory inventory){
        inventoryRepo.save(inventory);
        log.info("the data has been saved with ID {}",inventory.getId());
    }*/
}

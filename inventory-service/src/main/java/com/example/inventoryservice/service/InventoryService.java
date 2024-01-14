package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.Repository.InventoryRepo;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.model.OrderLineItemDto;
import com.example.inventoryservice.model.OrderLineItems;
import com.example.inventoryservice.model.OrderRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class InventoryService {//----------------------*---------*--------*------------------------------------------------------------
    //this service implements stock business logic and -----communicates----- with the order service to check is the requested order in present or not in the stock and then decreases it quantity
   //--------------------------------------*----------*--------*-------------------------------------------------------------
   private final InventoryRepo inventoryRepo;
   /* @Transactional(readOnly = true)*/
    public List<InventoryResponse> isInStock(/*List<String> skuCodeList,*/ OrderRequest orderRequest){
       List<String> skuCodeList1 =  getSkuCodeList(orderRequest);
       List<InventoryResponse> inventoryResponses = inventoryRepo.findBySkuCodeIn(skuCodeList1)
                .stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).collect(toList());//#for each skucode it will bring all the information and will create a InventoryResponse object for each inventory.
        if(!inventoryResponses.isEmpty()){
            updateDataBase(orderRequest,skuCodeList1);
        }
        return inventoryResponses;
    }

    private List<String> getSkuCodeList(OrderRequest orderRequest){
       return orderRequest.getOrderLineItemDtoList()
                .stream()
                .map(OrderLineItemDto::getSkuCode)
                .toList();

    }

    private void updateDataBase(OrderRequest orderRequest,List<String> skuCodeList){
        List<Inventory> inventoryResponses = inventoryRepo.findBySkuCodeIn(skuCodeList);
        inventoryResponses.forEach(inventory -> {
            orderRequest.getOrderLineItemDtoList().stream()
                    .filter(orderItem -> inventory.getSkuCode().equals(orderItem.getSkuCode()))
                    .findFirst()
                    .ifPresent(orderItem -> inventory.setQuantity(inventory.getQuantity() - orderItem.getQuantity()));
        });
        Iterable<Inventory> save =      inventoryRepo.saveAllAndFlush(inventoryResponses);
    }

}


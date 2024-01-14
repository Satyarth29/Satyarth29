package com.example.OrderService.service;

import com.example.OrderService.Repository.OrderRepository;
import com.example.OrderService.dto.InventoryResponse;
import com.example.OrderService.dto.OrderLineItemDto;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    //constructor injections
    private final WebClient.Builder webClientBuilder;
     private final OrderRepository orderRepository;
     public void placeOrder(OrderRequest orderRequest){
        Order order =  createOrderModel(orderRequest);
        //now I have to collect all the skuCodes from an order because an order can have multiple skuCodes
        List<String> skuCodeList = order.getOrderLineItemsList()
             .stream()
             .map(OrderLineItems::getSkuCode)
             .toList();

        InventoryResponse[] inventoryResponse =  getInventoryResponse(orderRequest,skuCodeList); //taking the response here microservice talking to each other
        log.info("Received inventory RESPONSE HERE {}", (Object) inventoryResponse);
        //inventory response arrays is getting converted into a stream, and it will check that the isInStock is true for all object or orderlinesitems
        boolean allProductsInStock =   Arrays.stream(inventoryResponse).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock){
           //############################
                orderRepository.save(order);
            // ############################saving the actual order here!!!
          List<String> notaProduct = skuCodeList.stream()//Use Case for when placed order is not there is the Repository.
                  .filter(i -> Arrays.stream(inventoryResponse).noneMatch(j -> j.getSkuCode().equals(i)))
                  .toList();
            if (!notaProduct.isEmpty()){
              log.info("Oops!!!! The Product {} that you're ordering is not registered as a Product in our System,therefore NOT PLACED!!can you re-check the Available products please,Thanks😊", notaProduct);
            }
        }
        else{
            List<String> outOfStock = Arrays.stream(inventoryResponse)
                  .filter(response -> !response.isInStock() && !response.getSkuCode().isEmpty())
                  .map(InventoryResponse::getSkuCode)
                  .toList();
          log.info("ALAS!!☄☄☄ {} are currently Unavailable in Stock,Please try again 😐",outOfStock);
          throw new IllegalArgumentException();}
    }
    private Order createOrderModel(OrderRequest orderRequest){
        Order order =  new Order();
        order.setOrderNumber(UUID.randomUUID().toString());//setting order-number
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemDtoList()
                .stream()
                .filter(i->i.getQuantity()>0 && i.getSkuCode()!= null && i.getPrice()>0.0)
                .map(this::mapFromDto)  //So, is a way of referencing and invoking the mapFromDto method on the current object (instance).
                .toList();              // It allows you to apply the method as a transformation logic on each element of the stream.

        if(!orderLineItems.isEmpty()){
            order.setOrderLineItemsList(orderLineItems);//order is ready!!
        }
        else{
            throw new NullPointerException("Pssss...null values not allowed!!! please place order with valid values,🙄");}
        return order;
    }
    private InventoryResponse[] getInventoryResponse(OrderRequest orderRequest,List<String> skuCodeList) {
        return webClientBuilder.build().post()
                .uri("http://inventory-service/api/inventory")
                .bodyValue(orderRequest)
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


      /*    return webClientBuilder.build().get()
              .uri(uriBuilder -> uriBuilder
                    .uri("http://inventory-service/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode",skuCodeList).build())//the webclient will build the uri with query parametrs of skucode
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();*/
    }
    private OrderLineItems mapFromDto(OrderLineItemDto orderLineItemDto) {
        OrderLineItems orderLineItems  = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemDto.getPrice());
        orderLineItems.setQuantity(orderLineItemDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemDto.getSkuCode());
        return orderLineItems;
    }
}

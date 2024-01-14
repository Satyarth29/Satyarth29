package com.example.OrderService.Controller;

import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.service.OrderService;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor//this is done because final fields are initialized once only and it is necessary to initialize final fields,and there is no better way for
//initializing final fields then a constructor because it will be only called once when an object is created.

public class OrderController{

    private final OrderService orderService;//we are using final fields because its the best pratice for DI

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LoadBalanced
//    @CircuitBreaker(name = "inventory",fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
    orderService.placeOrder(orderRequest);
    return "Order Placed successfully";
 }
// public  String fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException){
//        return "OOPS!! something went wrong,please order after a while";
// }
}

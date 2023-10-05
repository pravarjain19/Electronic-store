package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.OrderRequestDto;
import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid  OrderRequestDto request){
        OrderDto order = orderService.createOrder(request);
        return  new ResponseEntity<>(order  , HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderid}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderid){
        orderService.removeOrder(orderid);

        ApiResponseMessage orderDeltedSuccessfully = ApiResponseMessage.builder()
                .response(HttpStatus.OK)
                .status(true)
                .message("Order deleted successfully").build();

        return  new ResponseEntity<>(orderDeltedSuccessfully , HttpStatus.OK);

    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrderByUserId(@PathVariable  String userId){
        List<OrderDto> allOrderOfUser = orderService.getAllOrderOfUser(userId);

        return  new ResponseEntity<>(allOrderOfUser , HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<PageResponse<OrderDto>> getAllOrder(
            @RequestParam(required = false , value = "pageSize", defaultValue = "10") int pageSize ,
            @RequestParam(required = false , value = "pageNumber" , defaultValue = "0") int pageNumber,
            @RequestParam(required = false , value = "sortBy" , defaultValue = "orderedDate") String sortBy ,
            @RequestParam(required = false , value = "sortDir" , defaultValue = "desc") String sortDir
    ){
        PageResponse<OrderDto> allOrders = orderService.getAllOrders(pageSize, pageNumber, sortBy, sortDir);

        return  new ResponseEntity<>(allOrders , HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public  ResponseEntity<OrderDto> updateOrder(@PathVariable @NotBlank String orderId , @RequestBody OrderRequestDto orderRequestDto){
        OrderDto orderDto = orderService.updateOrder(orderRequestDto, orderId);

        return  new ResponseEntity<>(orderDto , HttpStatus.CREATED);
    }

}

package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.OrderRequestDto;
import com.lcwd.electronic.store.dtos.PageResponse;

import java.util.List;

public interface OrderService {


   OrderDto createOrder(OrderRequestDto orderDto );

   void removeOrder(String orderId);

   List<OrderDto> getAllOrderOfUser(String userId);


   PageResponse<OrderDto> getAllOrders(int pageSize , int pageNumber , String sortBy , String sortDir );


   OrderDto updateOrder(OrderRequestDto orderDto , String orderId );



}

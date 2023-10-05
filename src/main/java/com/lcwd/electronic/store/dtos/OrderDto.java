package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entites.OrderItem;
import com.lcwd.electronic.store.entites.User;
import lombok.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {


    private String  orderId;


    private  String orderStatus = "PENDING";

    private  String paymentStatus = "NOTPAID";

    private int orderAmount;

    private  String billingAddress;

    private  String billingPhone;

    private  String billingName;

    private Date orderedDate ;

    private  Date deliveryDate;

    private UserDto user;

    private List<OrderItemDto> orderItems = new ArrayList<>();
}

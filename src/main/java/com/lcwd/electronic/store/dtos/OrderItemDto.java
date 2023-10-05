package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entites.Order;
import com.lcwd.electronic.store.entites.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

    private  String orderItemId;

    private  int quantity;

    private  int totalPrice;


    private ProductDto product;


}

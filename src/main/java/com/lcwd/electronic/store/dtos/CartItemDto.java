package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.exceptions.BadRequest;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private  String cartItemId;



    private ProductDto product;
    @Size(min=1 , message = "quanity must be added")
    private int quantity;

    private int totalPrice;

}

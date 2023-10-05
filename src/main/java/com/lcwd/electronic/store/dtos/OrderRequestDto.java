package com.lcwd.electronic.store.dtos;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {



    private  String orderStatus ;

    private  String paymentStatus ;

    @NotBlank(message = "cart Id requried")
   private  String cartId;

    @NotBlank(message = "user Id requried")
   private  String  userId;


    @NotBlank(message = "billing address requried")
    private  String billingAddress;

    @NotBlank(message = "billing phone requried")

    private  String billingPhone;
    @NotBlank(message = "billing name requried")

    private  String billingName;


}

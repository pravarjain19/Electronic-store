package com.lcwd.electronic.store.entites;


import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    private  String cartItemId;


    @OneToOne
    @JoinColumn(name = "product_id")
    private  Product product;

    private int quantity;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;



}

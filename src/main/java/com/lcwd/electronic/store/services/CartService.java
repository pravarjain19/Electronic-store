package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {


    CartDto addItemtoCart(String userId , AddItemToCartRequest request);

    void removeItemtoCart(String userId , String cartItemid);


    void clearCart(String userId);

    CartDto getCartByUser(String userId);
}

package com.lcwd.electronic.store.controllers;


import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {


    @Autowired
    CartService cartService;
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemtoCart(@PathVariable String userId , @RequestBody AddItemToCartRequest request){
        CartDto cartDto = cartService.addItemtoCart(userId, request);
        return  new ResponseEntity<>(cartDto , HttpStatus.OK);
    }

    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String itemId , @PathVariable String userId){
        cartService.removeItemtoCart(userId , itemId);
        ApiResponseMessage itemRemovedFromTheCart = ApiResponseMessage.builder()
                .message("Item removed from the cart")
                .status(true)
                .response(HttpStatus.OK).build();

        return  new ResponseEntity<>(itemRemovedFromTheCart , HttpStatus.OK);

    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart( @PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage itemRemovedFromTheCart = ApiResponseMessage.builder()
                .message("Cart delete for the user")
                .status(true)
                .response(HttpStatus.OK).build();

        return  new ResponseEntity<>(itemRemovedFromTheCart , HttpStatus.OK);

    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart( @PathVariable String userId){
        CartDto cartByUser = cartService.getCartByUser(userId);


        return  new ResponseEntity<>(cartByUser , HttpStatus.OK);

    }





}

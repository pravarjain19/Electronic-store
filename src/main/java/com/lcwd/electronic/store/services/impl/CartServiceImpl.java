package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entites.Cart;
import com.lcwd.electronic.store.entites.CartItem;
import com.lcwd.electronic.store.entites.Product;
import com.lcwd.electronic.store.entites.User;
import com.lcwd.electronic.store.exceptions.BadRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemtoCart(String userId, AddItemToCartRequest request) {

        int quanity = request.getQuanity();
        String productId = request.getProductId();

        if(quanity <= 0){
                throw new BadRequest("quanity is not valid");
        }

        // fetch the product

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found " + productId));


//        every user have a single cart fetch the cart if cart is not ava-libel create one cart for that user

        // fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + userId));


            
//        fetch cart releted to the user and if cart is not there it will through an exception at that time
//        we will create a cart

        Cart cart = null;

        try{
            cart = cartRepository.findByUser(user).get();

        }catch (NoSuchElementException e){

//            creating a new cart
            cart = new Cart();
            cart.setCreatedAt(new Date());
            cart.setUser(user);
            cart.setCartId(UUID.randomUUID().toString());


        }

        AtomicBoolean isNewItemAdded = new AtomicBoolean(false);

        List<CartItem> items = cart.getItems();

        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity( quanity);
                item.setTotalPrice(item.getQuantity() * product.getDiscountedPrice());
                isNewItemAdded.set(true);
            }
            return item;
        }).collect(Collectors.toList());



        if(!isNewItemAdded.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quanity)
                    .product(product)
                    .totalPrice(product.getDiscountedPrice() * quanity)
                    .cartItemId(UUID.randomUUID().toString())
                    .cart(cart).build();

            cart.getItems() .add(cartItem);
        }



        Cart save = cartRepository.save(cart);


        return modelMapper.map(save , CartDto.class);
    }

    @Override
    public void removeItemtoCart(String userId, String cartItemid) {
        CartItem cartItem = cartItemRepository.findById(cartItemid)
                .orElseThrow(() -> new ResourceNotFoundException(" cartitem not found " + cartItemid));
        cartItemRepository.delete(cartItem);




    }

    @Override
    public void clearCart(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + userId));

        Cart cart = cartRepository.findByUser(user).get();
        return  modelMapper.map(cart , CartDto.class);
    }

    private final ModelMapper modelMapper = new ModelMapper();
}

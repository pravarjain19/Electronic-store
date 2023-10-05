package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.OrderRequestDto;
import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.entites.*;
import com.lcwd.electronic.store.exceptions.BadRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import com.lcwd.electronic.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    private  ModelMapper modelMapper = new ModelMapper();
    @Override
    public OrderDto createOrder(OrderRequestDto orderRequestDtoDto ) {

        String  userId = orderRequestDtoDto.getUserId();
        String cartId = orderRequestDtoDto.getCartId();
        OrderDto orderDto = new OrderDto();
        orderDto.setBillingAddress(orderRequestDtoDto.getBillingAddress());
        orderDto.setBillingPhone(orderRequestDtoDto.getBillingPhone());
        orderDto.setBillingName(orderRequestDtoDto.getBillingName());
        Order order = modelMapper.map(orderDto, Order.class);


        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException("No user found"+userId));
        Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                new ResourceNotFoundException("Cart not found" + cartId));


        List<CartItem> cartItems = cart.getItems();

        if(cartItems.size()<=0){
            throw  new BadRequest("invalid number of items in cart");
        }

        AtomicReference<Integer> totalOrderPrice = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID().toString());
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            totalOrderPrice.set(totalOrderPrice.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setUser(user);
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderAmount(totalOrderPrice.get());
        order.setOrderedDate(new Date());
        order.setDeliveryDate(null);

        cartService.clearCart(userId);
        Order saved = orderRepository.save(order);
        OrderDto map = modelMapper.map(saved, OrderDto.class);
        return map;
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("No order Found " + orderId));

        orderRepository.delete(order);

    }

    @Override
    public List<OrderDto> getAllOrderOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("user not found" + userId));
        List<Order> byUser = orderRepository.findByUser(user);

        if(byUser.size()<=0){
            throw  new ResourceNotFoundException("No order found by user "+ userId);
        }

        List<OrderDto> orderDtoList = byUser.stream()
                .map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());

        return orderDtoList;
    }

    @Override
    public PageResponse<OrderDto> getAllOrders(int pageSize, int pageNumber, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending())
                : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber , pageSize , sort);
        Page<Order> allOrders = orderRepository.findAll(pageable);

        return Helper.getPageableResponse(allOrders , OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(OrderRequestDto orderDto ,String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("NO order found by the id " + orderId));


        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(orderDto.getOrderStatus());

        Order saved = orderRepository.save(order);
        return  modelMapper.map(saved , OrderDto.class);
    }
}

package com.lcwd.electronic.store.entites;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Orders")
@Entity
public class Order {

    @Id
    private String  orderId;

    @NotBlank()
    private  String orderStatus;

    private  String paymentStatus;

    private int orderAmount;

    @Column(length = 1000)
    private  String billingAddress;


    private  String billingPhone;

    private  String billingName;

    private Date orderedDate;

    private  Date deliveryDate;


//    user
    @ManyToOne(fetch = FetchType.EAGER )
    @JoinColumn(name = "user_id")
    private  User user;

    @OneToMany(fetch = FetchType.EAGER , mappedBy = "order" ,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}

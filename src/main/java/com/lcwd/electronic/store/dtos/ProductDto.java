package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entites.Category;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {


    private  String productId;

    @NotBlank(message = "Add title")
    @Size(min = 3 , max = 15 , message = "Invalid Title")
    private  String title;

    @NotBlank(message = "Add Description ")
    private  String description;

    private  int price;

    private  int discountedPrice;

    private int quantity;

    @DateTimeFormat
    private Date addedDate;

    private boolean live;

    private  boolean stock;

    private  String productImageName;

    private CategoryDto category;

}

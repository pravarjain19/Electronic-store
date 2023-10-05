package com.lcwd.electronic.store.entites;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "categories")
public class Category
{
    @Id
    @Column(name = "category_id")
    private String categoryId;
    @Column(nullable = false , length = 50 , name = "category_title")
    private String title;
    @Column(length = 255 , name = "category_desc")
    private String description;

    @Column(name = "cover_image")
    private String coverImage;

    @OneToMany(mappedBy = "category"  ,  cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
//    other attribute if you want
}

package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder

public class CategoryDto {

    private String categoryId;
    @NotBlank(message = "Add title")
    @Size(min = 3 , max = 15 , message = "Invalid Title")

    private String title;
    @NotBlank(message = "Add some description to the category")
    private String description;
    @ImageNameValid(message = "Image is not valid")
    private String coverImage;

}

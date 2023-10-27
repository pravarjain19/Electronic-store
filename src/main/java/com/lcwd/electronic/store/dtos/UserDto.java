package com.lcwd.electronic.store.dtos;


import com.lcwd.electronic.store.entites.Role;
import com.lcwd.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {


    private String userId;
    @Size(min=3 , max=15 , message = "Invalid name")
    private String name;

//    @Email(message = "Enter Valid mail")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$" , message = "Invalid User Email")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is requried")
    private String password;


    private String about;


    private String imageName;
    @Size(min = 4 , max = 6 , message = "Invalid gender")
    private String gender;

    private Set<RoleDto> roles = new HashSet<>();

}

package com.lcwd.electronic.store.dtos;


import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {

    private  String email;
    private String password;
}

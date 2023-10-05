package com.lcwd.electronic.store.dtos;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class JwtResponse {

    private String jwtToken;
    private UserDto userDto;

}

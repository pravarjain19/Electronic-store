package com.lcwd.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMessage {

    public String message;
    public Boolean status;
     public HttpStatus response;
}

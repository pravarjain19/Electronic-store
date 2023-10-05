package com.lcwd.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageApiResponseMessage {
    public String imageName;
    public String message;
    public Boolean status;
    public HttpStatus response;
}

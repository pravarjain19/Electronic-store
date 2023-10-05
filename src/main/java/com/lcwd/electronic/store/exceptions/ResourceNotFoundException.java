package com.lcwd.electronic.store.exceptions;


import lombok.Builder;

@Builder
public class ResourceNotFoundException extends  RuntimeException{

   public ResourceNotFoundException(){
        super("Resource not Found exception");

    }
   public ResourceNotFoundException(String message){
        super(message);
    }
}

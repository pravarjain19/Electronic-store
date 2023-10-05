package com.lcwd.electronic.store.exceptions;

public class BadRequest extends  RuntimeException{

    public BadRequest(String message){
        super(message);
    }

   public BadRequest(){
        super("Invalid Request");
    }
}

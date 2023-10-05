package com.lcwd.electronic.store.exceptions;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {

        logger.info("Global Exception called ");
        return new ResponseEntity<>(ApiResponseMessage.builder()
                .message(exception.getMessage())
                .status(true)
                .response(HttpStatus.NOT_FOUND)
                .build()
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgsNotValidExceptionHandler(MethodArgumentNotValidException mex) {
        List<ObjectError> allErrors = mex.getBindingResult().getAllErrors();

        Map<String, Object> respose = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String defaultMessage = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();

            respose.put(field, defaultMessage);

        });


        return new ResponseEntity<>(respose, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ApiResponseMessage> invalidRequest(ResourceNotFoundException exception) {

        logger.info("Global Exception called ");
        return new ResponseEntity<>(ApiResponseMessage.builder()
                .message(exception.getMessage())
                .status(false)
                .response(HttpStatus.BAD_REQUEST)
                .build()
                , HttpStatus.BAD_REQUEST);
    }




}

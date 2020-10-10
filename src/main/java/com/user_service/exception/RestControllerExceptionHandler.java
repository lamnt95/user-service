package com.user_service.exception;

import com.user_service.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handlerException(ResourceNotFoundException e){
        ApiResponse response = e.getResponse();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

package com.prgms.springbootrestapi.exception;

import com.prgms.springbootrestapi.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BindingExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseDto<Object> bindExceptionHandler(BindException e) {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResponseDto.of(HttpStatus.BAD_REQUEST, message);
    }

}

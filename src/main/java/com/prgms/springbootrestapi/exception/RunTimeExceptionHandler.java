package com.prgms.springbootrestapi.exception;

import com.prgms.springbootrestapi.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RunTimeExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseDto<Object> runtimeExceptionHandler(RuntimeException e) {
        return ResponseDto.of(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

}

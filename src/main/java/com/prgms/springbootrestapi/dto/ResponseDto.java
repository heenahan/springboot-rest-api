package com.prgms.springbootrestapi.dto;

import org.springframework.http.HttpStatus;

public record ResponseDto<T>(
    int code,
    String message,
    T data
) {

    public static <T> ResponseDto<T> of(HttpStatus status, String message, T data) {
        return new ResponseDto<>(status.value(), message, data);
    }
    public static <T> ResponseDto<T> of(HttpStatus status, String message) {
        return new ResponseDto<>(status.value(), message, null);
    }

}

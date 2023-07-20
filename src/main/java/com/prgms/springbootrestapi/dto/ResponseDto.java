package com.prgms.springbootrestapi.dto;

public record ResponseDto<T>(
    T data
) {
}

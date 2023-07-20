package com.prgms.springbootrestapi.dto;

public record BoardGameUpdateDto(
    String name,
    int max,
    int min,
    int best,
    String category,
    int playTime,
    int complexity,
    String description
) {

}

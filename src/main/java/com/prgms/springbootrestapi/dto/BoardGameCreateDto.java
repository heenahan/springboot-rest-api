package com.prgms.springbootrestapi.dto;

import com.prgms.reactspringbootrestapi.domain.BoardGame;
import com.prgms.reactspringbootrestapi.domain.Category;
import com.prgms.reactspringbootrestapi.domain.Personnel;

import java.util.UUID;

public record BoardGameCreateDto(
    String name,
    int max,
    int min,
    int best,
    String category,
    int playTime,
    int complexity,
    String description
) {

    public BoardGame toEntity() {
        Personnel personnel = new Personnel(max, min, best);
        Category categoryOfValue = Category.of(category);
        return new BoardGame(UUID.randomUUID(), name, personnel, categoryOfValue, playTime, complexity, description);
    }

}

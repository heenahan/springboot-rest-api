package com.prgms.springbootrestapi.dto;

import com.prgms.springbootrestapi.domain.BoardGame;
import com.prgms.springbootrestapi.domain.Category;
import com.prgms.springbootrestapi.domain.Personnel;

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

    public BoardGame toEntity(UUID id) {
        Personnel personnel = new Personnel(max, min, best);
        Category categoryOfValue = Category.of(category);
        return new BoardGame(id, name, personnel, categoryOfValue, playTime, complexity, description);
    }

}

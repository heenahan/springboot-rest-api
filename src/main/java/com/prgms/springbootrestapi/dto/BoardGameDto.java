package com.prgms.springbootrestapi.dto;

import com.prgms.springbootrestapi.domain.BoardGame;
import com.prgms.springbootrestapi.domain.Category;
import com.prgms.springbootrestapi.domain.Personnel;

import java.util.UUID;

public record BoardGameDto(
    UUID id,
    String name,
    Personnel personnel,
    String category,
    int playTime,
    int complexity,
    String description
) {

    public static BoardGameDto from(BoardGame boardGame) {
        Category category = boardGame.getCategory();
        return new BoardGameDto(boardGame.getId(), boardGame.getName(), boardGame.getPersonnel(),
            category.getDisplayName(), boardGame.getPlayTime(), boardGame.getComplexity(), boardGame.getDescription());
    }

}

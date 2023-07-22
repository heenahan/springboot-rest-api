package com.prgms.springbootrestapi.dto;

import com.prgms.springbootrestapi.domain.BoardGame;
import com.prgms.springbootrestapi.domain.Category;
import com.prgms.springbootrestapi.domain.Personnel;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public record BoardGameCreateDto(
    @NotBlank(message = "보드게임 이름이 빈칸이어서는 안됩니다.")
    @Length(max = 50, message = "보드게임 이름은 50자를 넘어선 안됩니다.")
    String name,
    @Range(min = 1, max = 12, message = "보드게임 최대 인원은 1 - 12 사이의 숫자여야 합니다.")
    int max,
    @Range(min = 1, max = 12, message = "보드게임 최소 인원은 1 - 12 사이의 숫자여야 합니다.")
    int min,
    @Range(min = 1, max = 12, message = "보드게임 추천 인원은 1 - 12 사이의 숫자여야 합니다.")
    int best,
    @NotBlank(message = "보드게임 카테고리가 빈칸이어서는 안됩니다.")
    String category,
    @Range(min = 1, max = 1000, message = "플레이 타임은 1 - 1000 사이의 숫자여야 합니다.")
    int playTime,
    @Range(min = 1, max = 5, message = "보드게임의 복잡도는 1 - 5 사이의 숫자여야 합니다.")
    int complexity,
    @Length(max = 1000, message = "보드게임 설명은 1000자를 넘어선 안됩니다.")
    String description
) {

    public BoardGame toEntity(UUID id) {
        Personnel personnel = new Personnel(max, min, best);
        Category categoryOfValue = Category.of(category);
        return new BoardGame(id, name, personnel, categoryOfValue, playTime, complexity, description);
    }

}

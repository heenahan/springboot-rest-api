package com.prgms.springbootrestapi.application;

import com.prgms.springbootrestapi.domain.Category;
import com.prgms.springbootrestapi.domain.Personnel;
import com.prgms.springbootrestapi.dto.BoardGameCreateDto;
import com.prgms.springbootrestapi.dto.BoardGameDto;
import com.prgms.springbootrestapi.dto.BoardGameSearchDto;
import com.prgms.springbootrestapi.dto.BoardGameUpdateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
class BoardGameServiceTest {

    @Autowired
    private BoardGameService boardGameService;

    @Test
    @DisplayName("보드게임 저장 후 아이디로 조회하여 확인한다.")
    void 보드게임을_저장한다(){
        // given
        UUID boardGameId = UUID.randomUUID();
        BoardGameCreateDto boardGameCreateDto = new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 20, 1, "즐거운 게임");
        boardGameService.create(boardGameId, boardGameCreateDto);

        // when
        BoardGameDto findBoardGame = boardGameService.findOne(boardGameId);

        // then
        assertThat(findBoardGame).extracting("name", "category", "playTime", "complexity", "description")
            .contains("할리갈리", "가족", 20, 1, "즐거운 게임");
        assertThat(findBoardGame).extracting("personnel")
            .isEqualTo(new Personnel(10, 2, 5));
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 조회하면 에러를 던진다.")
    void 조회_에러() {
        // given
        UUID id = UUID.randomUUID();

        // when
        assertThatThrownBy(() -> {
            boardGameService.findOne(id);
        }).isInstanceOf(IllegalStateException.class)
        .hasMessage("존재하지 않는 보드게임입니다.");
    }

    @ParameterizedTest
    @MethodSource("filterAndResult")
    @DisplayName("카테고리와 추천 인원으로 필터 조회한 뒤 카테고리와 인원을 검증한다.")
    void 보드게임을_필터_조회한다(String categoryName, int best, int size, List<Category> categories, List<Personnel> personnels) {
        // given
        보드게임을_여러개_등록한다();
        BoardGameSearchDto boardGameSearchDto = new BoardGameSearchDto(categoryName, best);

        // when
        List<BoardGameDto> boardGames = boardGameService.search(boardGameSearchDto);

        // then
        assertThat(boardGames).hasSize(size);
        assertThat(boardGames).extracting("category")
            .containsAll(categories);
        assertThat(boardGames).extracting("personnel")
            .containsAll(personnels);
    }

    @Test
    @DisplayName("보드게임의 이름, 인원, 카테고리, 플레이 타임, 복잡도, 설명을 수정한다.")
    void 보드게임_수정() {
        // given
        UUID id = UUID.randomUUID();
        BoardGameCreateDto boardGameCreateDto = new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 20, 1, "즐거운 게임");
        boardGameService.create(id, boardGameCreateDto);
        BoardGameUpdateDto boardGameUpdateDto = new BoardGameUpdateDto("루미큐브", 4, 2, 4, "party", 45, 1, "행복한 게임");

        // when
        boardGameService.update(id, boardGameUpdateDto);

        // then
        BoardGameDto updateBoardGame = boardGameService.findOne(id);
        assertThat(updateBoardGame).extracting("name", "category", "playTime", "complexity", "description")
            .contains("루미큐브", "파티", 45, 1, "행복한 게임");
        assertThat(updateBoardGame).extracting("personnel")
            .isEqualTo(new Personnel(4, 2, 4));
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 보드게임을 수정하면 에러를 던진다.")
    void 보드게임_수정_에러() {
        // given
        UUID id = UUID.randomUUID();
        BoardGameUpdateDto boardGameUpdateDto = new BoardGameUpdateDto("루미큐브", 4, 2, 4, "party", 45, 1, "행복한 게임");

        // when
        assertThatThrownBy(() -> {
            boardGameService.update(id, boardGameUpdateDto);
        }).isInstanceOf(IllegalStateException.class)
        .hasMessage("존재하지 않는 보드게임입니다.");
    }

    @Test
    @DisplayName("보드게임을 삭제하면 조회했을 때 조회되지 않아야 한다.")
    void 보드게임을_삭제한다() {
        // given
        UUID id = UUID.randomUUID();
        BoardGameCreateDto boardGameCreateDto = new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 20, 1, "즐거운 게임");
        boardGameService.create(id, boardGameCreateDto);

        // when
        boardGameService.deleteOne(id);

        // then
        assertThatThrownBy(() -> {
            boardGameService.findOne(id);
        }).isInstanceOf(IllegalStateException.class)
            .hasMessage("존재하지 않는 보드게임입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 보드게임을 삭제하면 에러를 던진다.")
    void 보드게임_삭제_에러() {
        // given
        UUID id = UUID.randomUUID();

        assertThatThrownBy(() -> {
            boardGameService.deleteOne(id);
        }).isInstanceOf(IllegalStateException.class)
            .hasMessage("존재하지 않는 보드게임입니다.");
    }

    /**
     * categoy
     * family : 2 | party : 2
     * best
     * 5명 : 2 | 2명 : 2
     */
    private void 보드게임을_여러개_등록한다() {
        BoardGameCreateDto boardGame1 = new BoardGameCreateDto("name1", 10, 2, 5, "family", 100, 2, null);
        boardGameService.create(UUID.randomUUID(), boardGame1);

        BoardGameCreateDto boardGame2 = new BoardGameCreateDto("name2", 10, 2, 5, "party", 100, 2, null);
        boardGameService.create(UUID.randomUUID(), boardGame2);

        BoardGameCreateDto boardGame3 = new BoardGameCreateDto("name3", 4, 2, 2, "family", 50, 1, null);
        boardGameService.create(UUID.randomUUID(), boardGame3);

        BoardGameCreateDto boardGame4 = new BoardGameCreateDto("name4", 4, 2, 2, "party", 20, 2, null);
        boardGameService.create(UUID.randomUUID(), boardGame4);
    }

    static Stream<Arguments> filterAndResult() {
        return Stream.of(
            Arguments.of("none", 0, 4, List.of("가족", "파티"), List.of(new Personnel(10, 2, 5), new Personnel(4, 2, 2))),
            Arguments.of("family", 0, 2, List.of("가족"), List.of(new Personnel(10, 2, 5), new Personnel(4, 2, 2))),
            Arguments.of("none", 5, 2, List.of("가족", "파티"), List.of(new Personnel(10, 2, 5))),
            Arguments.of("party", 2, 1, List.of("파티"), List.of(new Personnel(4, 2, 2)))
        );
    }

}
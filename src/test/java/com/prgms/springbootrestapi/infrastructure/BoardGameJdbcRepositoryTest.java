package com.prgms.springbootrestapi.infrastructure;

import com.prgms.springbootrestapi.domain.BoardGame;
import com.prgms.springbootrestapi.domain.Category;
import com.prgms.springbootrestapi.domain.Personnel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트 후 롤백
class BoardGameJdbcRepositoryTest {
    
    @Autowired
    BoardGameRepository boardGameRepository;

    @Test
    @DisplayName("보드게임을 저장한다")
    void 보드게임을_저장한다() {
        // given
        Category category = Category.FAMILY;
        Personnel personnel = new Personnel(10, 2, 5);
        BoardGame boardGame = new BoardGame(UUID.randomUUID(), "name", personnel, category, 100, 2, null);

        // when
        boardGameRepository.save(boardGame);

        // then
        Optional<BoardGame> findBoardGame = boardGameRepository.findById(boardGame.getId());
        assertThat(findBoardGame)
            .isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .comparingOnlyFields("id", "name", "personnel", "category", "playTime", "complexity", "description")
            .isEqualTo(boardGame);
    }

    @ParameterizedTest
    @MethodSource("filterAndResult")
    @DisplayName("카테고리와 추천인원을 기준으로 필터링하여 조회한다.")
    void 보드게임에서_필터_조회한다(String categoryName, int best, int size, List<Category> categories, List<Integer> numbers) {
        // given
        Category category = Category.of(categoryName);
        보드게임을_여러개_등록한다();

        // when
        List<BoardGame> boardGames = boardGameRepository.findByCateogryAndBest(category, best);

        // then
        assertThat(boardGames).hasSize(size)
            .extracting("category")
            .containsAll(categories);

        assertThat(boardGames).extracting("personnel")
            .extracting("best")
            .containsAll(numbers);
    }

    @Test
    @DisplayName("보드게임을 수정한다.")
    void 보드게임을_수정한다() {
        // given
        Category category = Category.FAMILY;
        Personnel personnel = new Personnel(10, 2, 5);
        BoardGame boardGame = new BoardGame(UUID.randomUUID(), "name", personnel, category, 100, 2, null);
        boardGameRepository.save(boardGame);

        // when
        boardGame.updateAll("name2", personnel, category, 30, 1, "description");
        boardGameRepository.update(boardGame);

        // then
        Optional<BoardGame> updateBoardGame = boardGameRepository.findById(boardGame.getId());
        assertThat(updateBoardGame).isNotEmpty()
            .get()
            .usingRecursiveComparison()
            .comparingOnlyFields("name", "personnel", "category", "playTime", "complexity", "description")
            .isEqualTo(boardGame);
    }

    @Test
    @DisplayName("보드게임을 삭제한다.")
    void 보드게임을_삭제한다() {
        // given
        Category category = Category.FAMILY;
        Personnel personnel = new Personnel(10, 2, 5);
        BoardGame boardGame = new BoardGame(UUID.randomUUID(), "name", personnel, category, 100, 2, null);
        boardGameRepository.save(boardGame);

        // when
        boardGameRepository.deleteOne(boardGame);

        // then
        Optional<BoardGame> deleteBoardGame = boardGameRepository.findById(boardGame.getId());
        assertThat(deleteBoardGame).isEmpty();
    }

    /**
     * categoy
     * family : 2 | party : 2
     * best
     * 5명 : 2 | 2명 : 2
     */
    private void 보드게임을_여러개_등록한다() {
        Category family = Category.FAMILY;
        Personnel personnel1 = new Personnel(10, 2, 5);
        BoardGame boardGame1 = new BoardGame(UUID.randomUUID(), "name1", personnel1, family, 100, 3, null);
        boardGameRepository.save(boardGame1);

        Category party = Category.PARTY;
        BoardGame boardGame2 = new BoardGame(UUID.randomUUID(), "name2", personnel1, party, 100, 2, null);
        boardGameRepository.save(boardGame2);

        Personnel personnel2 = new Personnel(4, 2, 2);
        BoardGame boardGame3 = new BoardGame(UUID.randomUUID(), "name3", personnel2, family, 50, 4, null);
        boardGameRepository.save(boardGame3);

        BoardGame boardGame4 = new BoardGame(UUID.randomUUID(), "name4", personnel2, party, 50, 1, null);
        boardGameRepository.save(boardGame4);
    }

    static Stream<Arguments> filterAndResult() {
        return Stream.of(
            Arguments.of("none", 0, 4, List.of(Category.FAMILY, Category.PARTY), List.of(5, 2)),
            Arguments.of("family", 0, 2, List.of(Category.FAMILY), List.of(5, 2)),
            Arguments.of("none", 5, 2, List.of(Category.FAMILY, Category.PARTY), List.of(5)),
            Arguments.of("party", 2, 1, List.of(Category.PARTY), List.of(2))
        );
    }

}
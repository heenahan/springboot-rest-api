package com.prgms.springbootrestapi.infrastructure;

import com.prgms.springbootrestapi.domain.BoardGame;
import com.prgms.springbootrestapi.domain.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardGameRepository {

    void save(BoardGame boardGame);

    Optional<BoardGame> findById(UUID id);
    List<BoardGame> findByCateogryAndBest(Category category, int best);
    void update(BoardGame boardGame);
    void deleteOne(BoardGame boardGame);

}

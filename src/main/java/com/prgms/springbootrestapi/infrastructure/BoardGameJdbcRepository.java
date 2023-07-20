package com.prgms.springbootrestapi.infrastructure;

import com.prgms.reactspringbootrestapi.domain.BoardGame;
import com.prgms.reactspringbootrestapi.domain.Category;
import com.prgms.reactspringbootrestapi.domain.Period;
import com.prgms.reactspringbootrestapi.domain.Personnel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.prgms.reactspringbootrestapi.util.RepositoryUtil.biToUUID;
import static com.prgms.reactspringbootrestapi.util.RepositoryUtil.timeStampToLocalDateTime;

@Repository
public class BoardGameJdbcRepository implements BoardGameRepository {

    private static final String INSERT = "insert into boardgames(boardgame_id, name, category, max, min, best, play_time, complexity, description) " +
        "values (UUID_TO_BIN(:id), :name, :category, :max, :min, :best, :playTime, :complexity, :description)";
    private static final String FIND_ALL = "select * from boardgames";
    private static final String WHERE = "where";
    private static final String AND = "and";
    private static final String ID = "boardgame_id = :id";
    private static final String CATEGORY = "category = :category";
    private static final String BEST = "best = :best";
    private static final String UPDATE = "update boardgames set name = :name, category = :category, max = :max, min = :min, best = :best, play_time = :playTime, complexity = :complexity, description = :description " +
        "where boardgame_id = UUID_TO_BIN(:id)";
    private static final String DELETE_ONE = "delete from boardgames where boardgame_id = UUID_TO_BIN(:id)";


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BoardGameJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(BoardGame boardGame) {
        int result = jdbcTemplate.update(INSERT, sqlParameterSource(boardGame));
        if (result != 1) {
            throw new IllegalStateException("보드게임 저장에 실패했습니다.");
        }
    }

    @Override
    public Optional<BoardGame> findById(UUID id) {
        String query = String.format("%s %s %s", FIND_ALL, WHERE, ID);
        try {
            BoardGame boardGame = jdbcTemplate.queryForObject(query, Collections.singletonMap("id", id), BOARD_GAME_ROW_MAPPER);
            return Optional.ofNullable(boardGame);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BoardGame> findByCateogryAndBest(Category category, int best) {
        // 동적 쿼리
        StringBuilder query = new StringBuilder(FIND_ALL);
        if (category != Category.NONE && best > 0) {
            query.append(" " + WHERE);
        }
        if (category != Category.NONE) {
            query.append(" " + CATEGORY);
        }
        if (best > 0) {
            if (category != null) {
                query.append(" " + AND);
            }
            query.append(" " + BEST);
        }
        return jdbcTemplate.query(query.toString(), BOARD_GAME_ROW_MAPPER);
    }

    @Override
    public void update(BoardGame boardGame) {
        int result = jdbcTemplate.update(UPDATE, sqlParameterSource(boardGame));
        if (result != 1) {
            throw new IllegalStateException("보드게임 수정에 실패했습니다.");
        }
    }

    @Override
    public void deleteOne(UUID id) {
        int result = jdbcTemplate.update(DELETE_ONE, Collections.singletonMap("id", id));
        if (result != 1) {
            throw new IllegalStateException("보드게임 삭제에 실패했습니다.");
        }
    }

    private SqlParameterSource sqlParameterSource(BoardGame boardGame) {
        Personnel personnel = boardGame.getPersonnel();
        Category category = boardGame.getCategory();
        return new MapSqlParameterSource().addValue("id", boardGame.getId())
            .addValue("name", boardGame.getName())
            .addValue("max", personnel.max())
            .addValue("min", personnel.min())
            .addValue("best", personnel.best())
            .addValue("category", category.getName())
            .addValue("playTime", boardGame.getPlayTime())
            .addValue("complexity", boardGame.getComplexity())
            .addValue("description", boardGame.getDescription());
    }

    private final static RowMapper<BoardGame> BOARD_GAME_ROW_MAPPER = (resultSet, rowNumber) -> {
        UUID boardGameId = biToUUID(resultSet.getBytes("board_game_id"));
        String name = resultSet.getString("name");
        int max = resultSet.getInt("max");
        int min = resultSet.getInt("min");
        int best = resultSet.getInt("best");
        Category category = Category.of(resultSet.getString("category"));
        int playTime = resultSet.getInt("play_time");
        int complexity = resultSet.getInt("complexity");
        String description = resultSet.getString("description");
        LocalDateTime createdAt = timeStampToLocalDateTime(resultSet.getTimestamp("created_at"));
        LocalDateTime updatedAt = timeStampToLocalDateTime(resultSet.getTimestamp("updated_at"));

        Personnel personnel = new Personnel(max, min, best);
        Period period = new Period(createdAt, updatedAt);
        return new BoardGame(boardGameId, name, personnel, category, playTime, complexity, description, period);
    };

}
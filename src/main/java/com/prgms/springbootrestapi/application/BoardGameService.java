package com.prgms.springbootrestapi.application;

import com.prgms.springbootrestapi.domain.BoardGame;
import com.prgms.springbootrestapi.domain.Category;
import com.prgms.springbootrestapi.domain.Personnel;
import com.prgms.springbootrestapi.dto.BoardGameCreateDto;
import com.prgms.springbootrestapi.dto.BoardGameDto;
import com.prgms.springbootrestapi.dto.BoardGameSearchDto;
import com.prgms.springbootrestapi.dto.BoardGameUpdateDto;
import com.prgms.springbootrestapi.infrastructure.BoardGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

    public BoardGameService(BoardGameRepository boardGameRepository) {
        this.boardGameRepository = boardGameRepository;
    }

    @Transactional
    public void create(UUID boardGameId, BoardGameCreateDto boardGameCreateDto) {
        BoardGame boardGame = boardGameCreateDto.toEntity(boardGameId);
        boardGameRepository.save(boardGame);
    }

    public BoardGameDto findOne(UUID id) {
        Optional<BoardGame> boardGameOfOptional = boardGameRepository.findById(id);
        BoardGame boardGame = boardGameOfOptional.orElseThrow(() -> new IllegalStateException("존재하지 않는 보드게임입니다."));
        return BoardGameDto.from(boardGame);
    }

    public List<BoardGameDto> search(BoardGameSearchDto boardGameSearchDto) {
        String categoryOfName = boardGameSearchDto.category();
        Category category = Category.of(categoryOfName);
        int best = boardGameSearchDto.best();
        List<BoardGame> boardGames = boardGameRepository.findByCateogryAndBest(category, best);
        return boardGames.stream()
                    .map(BoardGameDto::from)
                    .toList();
    }

    @Transactional
    public void update(UUID id, BoardGameUpdateDto boardGameUpdateDto) {
        Optional<BoardGame> boardGameOfOptional = boardGameRepository.findById(id);
        BoardGame boardGame = boardGameOfOptional.orElseThrow(() -> new IllegalStateException("존재하지 않는 보드게임입니다."));
        Personnel personnel = new Personnel(boardGameUpdateDto.max(), boardGameUpdateDto.min(), boardGameUpdateDto.best());
        Category category = Category.of(boardGameUpdateDto.category());
        boardGame.updateAll(boardGameUpdateDto.name(), personnel, category, boardGameUpdateDto.playTime(), boardGameUpdateDto.complexity(), boardGameUpdateDto.description());
        boardGameRepository.update(boardGame);
    }

    @Transactional
    public void deleteOne(UUID id) {
        Optional<BoardGame> boardGameOfOptional = boardGameRepository.findById(id);
        BoardGame boardGame = boardGameOfOptional.orElseThrow(() -> new IllegalStateException("존재하지 않는 보드게임입니다."));
        boardGameRepository.deleteOne(boardGame);
    }

}

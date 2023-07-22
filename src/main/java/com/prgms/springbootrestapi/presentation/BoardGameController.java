package com.prgms.springbootrestapi.presentation;

import com.prgms.springbootrestapi.application.BoardGameService;
import com.prgms.springbootrestapi.dto.BoardGameCreateDto;
import com.prgms.springbootrestapi.dto.BoardGameDto;
import com.prgms.springbootrestapi.dto.BoardGameSearchDto;
import com.prgms.springbootrestapi.dto.BoardGameUpdateDto;
import com.prgms.springbootrestapi.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/boardgame/")
public class BoardGameController {

    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping
    public ResponseEntity<String> create(BoardGameCreateDto boardGameCreateDto) {
        boardGameService.create(UUID.randomUUID(), boardGameCreateDto);
        return new ResponseEntity<>("저장에 성공했습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardGameDto> findDetail(@PathVariable UUID id) {
        BoardGameDto boardGame = boardGameService.findOne(id);
        return new ResponseEntity<>(boardGame, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> search(@RequestParam(defaultValue = "none") String category,
                                              @RequestParam(defaultValue = "0") int best) {
        BoardGameSearchDto boardGameSearchDto = new BoardGameSearchDto(category, best);
        List<BoardGameDto> boardGames = boardGameService.search(boardGameSearchDto);
        return new ResponseEntity<>(new ResponseDto(boardGames), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, BoardGameUpdateDto boardGameUpdateDto) {
        boardGameService.update(id, boardGameUpdateDto);
        return new ResponseEntity<>("수정에 성공했습니다.", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOne(@PathVariable UUID id) {
        boardGameService.deleteOne(id);
        return new ResponseEntity<>("삭제에 성공했습니다.", HttpStatus.NO_CONTENT);
    }

}

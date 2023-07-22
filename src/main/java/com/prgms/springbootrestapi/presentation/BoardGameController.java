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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/boardgame")
public class BoardGameController {

    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody @Valid BoardGameCreateDto boardGameCreateDto) {
        boardGameService.create(UUID.randomUUID(), boardGameCreateDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseDto<BoardGameDto> findDetail(@PathVariable UUID id) {
        BoardGameDto boardGame = boardGameService.findOne(id);
        return ResponseDto.of(HttpStatus.OK, "보드게임 조회에 성공했습니다.", boardGame);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseDto<List<BoardGameDto>> search(@RequestParam(defaultValue = "none") String category,
                                              @RequestParam(defaultValue = "0") int best) {
        BoardGameSearchDto boardGameSearchDto = new BoardGameSearchDto(category, best);
        List<BoardGameDto> boardGames = boardGameService.search(boardGameSearchDto);
        return ResponseDto.of(HttpStatus.OK, "보드게임 조회에 성공했습니다.", boardGames);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@PathVariable UUID id, @RequestBody @Valid BoardGameUpdateDto boardGameUpdateDto) {
        boardGameService.update(id, boardGameUpdateDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable UUID id) {
        boardGameService.deleteOne(id);
    }

}

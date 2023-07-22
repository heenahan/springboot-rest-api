package com.prgms.springbootrestapi.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgms.springbootrestapi.application.BoardGameService;
import com.prgms.springbootrestapi.dto.BoardGameCreateDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BoardGameController.class)
class BoardGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardGameService boardGameService;

    @ParameterizedTest
    @MethodSource("successBoardGameCreateDtos")
    void 보드게임_생성(BoardGameCreateDto boardGameCreateDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/boardgame")
                .content(objectMapper.writeValueAsString(boardGameCreateDto))
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("failBoardGameCreateDtos")
    void 보드게임_생성_에러(BoardGameCreateDto boardGameCreateDto, String message) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/boardgame")
            .content(objectMapper.writeValueAsString(boardGameCreateDto))
            .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value(message))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    static Stream<Arguments> successBoardGameCreateDtos() {
        Random random = new Random();
        String randomOfName = random.ints(96, 113)
            .limit(50)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        String randomOfDescription = random.ints(96, 113)
            .limit(1000)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        return Stream.of(
            Arguments.of(new BoardGameCreateDto("할리갈리", 1, 1, 1, "family", 1, 1, null)),
            Arguments.of(new BoardGameCreateDto(randomOfName, 12, 12, 12, "family", 1000, 5, randomOfDescription))
        );
    }

    static Stream<Arguments> failBoardGameCreateDtos() {
        Random random = new Random();
        String randomOfName = random.ints(96, 113)
            .limit(51)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        String randomOfDescription = random.ints(96, 113)
            .limit(1001)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        return Stream.of(
            Arguments.of(new BoardGameCreateDto(" ", 10, 2, 5, "family", 20, 1, "즐거운 게임"), "보드게임 이름이 빈칸이어서는 안됩니다."),
            Arguments.of(new BoardGameCreateDto(randomOfName, 10, 2, 5, "family", 20, 1, "즐거운 게임"), "보드게임 이름은 50자를 넘어선 안됩니다."),

            Arguments.of(new BoardGameCreateDto("할리갈리", 0, 2, 5, "family", 20, 1, "즐거운 게임"), "보드게임 최대 인원은 1 - 12 사이의 숫자여야 합니다."),
            Arguments.of(new BoardGameCreateDto("할리갈리", 13, 2, 5, "family", 20, 1, "즐거운 게임"), "보드게임 최대 인원은 1 - 12 사이의 숫자여야 합니다."),

            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 0, 5, "family", 20, 1, "즐거운 게임"), "보드게임 최소 인원은 1 - 12 사이의 숫자여야 합니다."),
            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 13, 5, "family", 20, 1, "즐거운 게임"), "보드게임 최소 인원은 1 - 12 사이의 숫자여야 합니다."),

            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 0, "family", 20, 1, "즐거운 게임"), "보드게임 추천 인원은 1 - 12 사이의 숫자여야 합니다."),
            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 13, "family", 20, 1, "즐거운 게임"), "보드게임 추천 인원은 1 - 12 사이의 숫자여야 합니다."),

            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 5, " ", 20, 1, "즐거운 게임"), "보드게임 카테고리가 빈칸이어서는 안됩니다."),

            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 0, 1, "즐거운 게임"), "플레이 타임은 1 - 1000 사이의 숫자여야 합니다."),
            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 1001, 1, "즐거운 게임"), "플레이 타임은 1 - 1000 사이의 숫자여야 합니다."),

            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 20, 0, "즐거운 게임"), "보드게임의 복잡도는 1 - 5 사이의 숫자여야 합니다."),
            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 20, 6, "즐거운 게임"), "보드게임의 복잡도는 1 - 5 사이의 숫자여야 합니다."),

            Arguments.of(new BoardGameCreateDto("할리갈리", 10, 2, 5, "family", 20, 1, randomOfDescription), "보드게임 설명은 1000자를 넘어선 안됩니다.")
        );
    }

}
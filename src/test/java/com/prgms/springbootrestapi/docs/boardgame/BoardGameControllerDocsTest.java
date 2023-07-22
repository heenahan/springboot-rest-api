package com.prgms.springbootrestapi.docs.boardgame;

import com.prgms.springbootrestapi.application.BoardGameService;
import com.prgms.springbootrestapi.docs.RestDocsSupport;
import com.prgms.springbootrestapi.domain.Personnel;
import com.prgms.springbootrestapi.dto.BoardGameCreateDto;
import com.prgms.springbootrestapi.dto.BoardGameDto;
import com.prgms.springbootrestapi.presentation.BoardGameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardGameControllerDocsTest extends RestDocsSupport {

    private final BoardGameService boardGameService = Mockito.mock(BoardGameService.class); // 서비스 mocking

    @Override
    protected Object initController() {
        return new BoardGameController(boardGameService);
    }

    @ParameterizedTest
    @MethodSource("successBoardGameCreateDtos")
    @DisplayName("보드게임을 생성하면 201 created를 응답으로 보낸다.")
    void 보드게임_생성(BoardGameCreateDto boardGameCreateDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/boardgame")
                .content(objectMapper.writeValueAsString(boardGameCreateDto))
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("board-game-create",
                preprocessRequest(prettyPrint()),
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .attributes(key("constraints").value("길이 50자 이하"))
                        .description("이름"),
                    fieldWithPath("max").type(JsonFieldType.NUMBER)
                        .attributes(key("constraints").value("범위 1 - 12"))
                        .description("최대 인원"),
                    fieldWithPath("min").type(JsonFieldType.NUMBER)
                        .attributes(key("constraints").value("범위 1 - 12"))
                        .description("최소 인원"),
                    fieldWithPath("best").type(JsonFieldType.NUMBER)
                        .attributes(key("constraints").value("범위 1 - 12"))
                        .description("추천 인원"),
                    fieldWithPath("category").type(JsonFieldType.STRING)
                        .description("카테고리"),
                    fieldWithPath("playTime").type(JsonFieldType.NUMBER)
                        .attributes(key("constraints").value("범위 1 - 1000"))
                        .description("플레이 시간"),
                    fieldWithPath("complexity").type(JsonFieldType.NUMBER)
                        .attributes(key("constraints").value("범위 1 - 5"))
                        .description("복잡도"),
                    fieldWithPath("description").type(JsonFieldType.VARIES)
                        .optional()
                        .attributes(key("constraints").value("길이 1000자 이하"))
                        .description("설명")
                )));
    }

    @Test
    @DisplayName("아이디로 보드게임을 찾는다")
    void 보드게임_조회() throws Exception {
        UUID id = UUID.randomUUID();
        Personnel personnel = new Personnel(10, 2, 5);
        BoardGameDto boardGameDto = new BoardGameDto(id, "할리갈리", personnel, "가족", 20, 1, "즐거운 게임");
        given(boardGameService.findOne(any(UUID.class)))
            .willReturn(boardGameDto);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/boardgame/{id}", id))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.message").value("보드게임 조회에 성공했습니다."))
            .andExpect(jsonPath("$.data").isNotEmpty())
            .andDo(document("board-game-find-one",
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("id").description("보드게임 아이디")),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                        .description("상태코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.STRING)
                        .description("보드게임 아이디"),
                    fieldWithPath("data.name").type(JsonFieldType.STRING)
                        .description("이름"),
                    fieldWithPath("data.personnel.max").type(JsonFieldType.NUMBER)
                        .description("최대 인원"),
                    fieldWithPath("data.personnel.min").type(JsonFieldType.NUMBER)
                        .description("최소 인원"),
                    fieldWithPath("data.personnel.best").type(JsonFieldType.NUMBER)
                        .description("추천 인원"),
                    fieldWithPath("data.category").type(JsonFieldType.STRING)
                        .description("카테고리"),
                    fieldWithPath("data.playTime").type(JsonFieldType.NUMBER)
                        .description("플레이 시간"),
                    fieldWithPath("data.complexity").type(JsonFieldType.NUMBER)
                        .description("복잡도"),
                    fieldWithPath("data.description").type(JsonFieldType.STRING)
                        .description("설명")
                )
            ));
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
            Arguments.of(new BoardGameCreateDto(randomOfName, 12, 12, 12, "family", 1000, 5, randomOfDescription)),
            Arguments.of(new BoardGameCreateDto("할리갈리", 4, 2, 4, "party", 20, 1, "즐거운 게임"))
        );
    }

}

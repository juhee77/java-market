package com.lahee.market.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahee.market.dto.RequestSalesItemDto;
import com.lahee.market.dto.ResponseSalesItemDto;
import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.DeleteCommentDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.CommentRepository;
import com.lahee.market.repository.SalesItemRepository;
import com.lahee.market.service.CommentService;
import com.lahee.market.service.SalesItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.lahee.market.constants.ControllerMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@ActiveProfiles("test")
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalesItemRepository salesItemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private SalesItemService salesItemService;
    @Autowired
    private CommentService commentService;

    private static SalesItem item;

    @Test
    @DisplayName("item 생성 조회 (POST /items/{itemId}/comment)")
    public void saveItem() throws Exception {
        //given
        //item 생성
        RequestCommentDto dto = getRequestCommentDto();
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        //when
        mockMvc.perform(post("/items/{itemId}/comments", item.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Comment/POST/comment 생성",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(SAVE_COMMENT_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        List<Comment> all = commentRepository.findAll();
        Comment comment = all.get(0);
        assertThat(comment.getSalesItem().getId()).isEqualTo(item.getId());
        assertThat(comment.getWriter()).isEqualTo("jeeho.edu");
        assertThat(comment.getContent()).isEqualTo("할인 가능하신가요?");
    }

    @Test
    @DisplayName("comment 페이징 조회 (GET /items/{itemId}/comments)")
    public void findPagedComment() throws Exception {
        //given
        String cWriter = "jeeho.edu";
        String cPassword = "qwerty1234";
        String cContent = "할인 가능하신가요?";
        for (int i = 0; i < 25; i++) {
            commentService.save(item.getId(), new RequestCommentDto("cWriter" + i, "cPassword" + i, "cContent" + i));
        }

        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}/comments", item.getId())
                        .param("page", String.valueOf(2))
                        .param("limit", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Comment/GET/comment 페이징 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

        //then (25개의 엘리먼트중 3쪽출력)
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("totalElements", equalTo(25)),
                jsonPath("totalPages", equalTo(3)),
                jsonPath("numberOfElements", equalTo(5))
        );
    }

    @Test
    @DisplayName("comment 단일건 조회 (GET /items/{itemId}/comments/{commentId})")
    public void findOneComment() throws Exception {
        //given
        RequestCommentDto reqDto = getRequestCommentDto();
        ResponseCommentDto save = commentService.save(item.getId(), reqDto);


        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}/comments/{commentId}", item.getId(), save.getId())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Comment/GET/comment 단일 건 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").value(save.getId()),
                jsonPath("$.content").value(save.getContent()),
                jsonPath("$.reply").value(save.getReply())
        );
    }

    @Test
    @DisplayName("comment 업데이트 확인 (PUT /items/{itemId}/comments/{commentId})")
    public void updateComment() throws Exception {
        //given
        String modify = "MODIFY";
        RequestCommentDto reqDto = getRequestCommentDto();
        ResponseCommentDto save = commentService.save(item.getId(), reqDto);

        RequestCommentDto updateDto = new RequestCommentDto(reqDto.getWriter(), reqDto.getPassword(), modify);
        String requestBody = new ObjectMapper().writeValueAsString(updateDto);

        //when
        mockMvc.perform(put("/items/{itemId}/comments/{commentId}", item.getId(), save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Comment/PUT/comment 업데이트",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(UPDATE_COMMENT_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        Comment comment = commentRepository.findById(save.getId()).get();
        assertThat(comment.getContent()).isEqualTo(modify);
    }


    @Test
    @DisplayName("comment reply 업데이트 확인 (PUT /items/{itemId}/comments/{commentId})/reply")
    public void updateCommentReply() throws Exception {
        //given
        String reply = "REPLY";
        RequestCommentDto reqDto = getRequestCommentDto();
        ResponseCommentDto save = commentService.save(item.getId(), reqDto);
        CommentReplyDto updateDto = new CommentReplyDto(item.getWriter(), item.getPassword(), reply);
        String requestBody = new ObjectMapper().writeValueAsString(updateDto);

        //when
        mockMvc.perform(put("/items/{itemId}/comments/{commentId}/reply", item.getId(), save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Comment/PUT/comment reply 업데이트",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(UPDATE_COMMENT_REPLY_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        Comment comment = commentRepository.findById(save.getId()).get();
        assertThat(comment.getReply()).isEqualTo(reply);
    }

    @Test
    @DisplayName("comment 삭제 확인 (DELETE /items/{itemId}/comments/{commentId})")
    public void deleteComment() throws Exception {
        //given
        RequestCommentDto reqDto = getRequestCommentDto();
        ResponseCommentDto save = commentService.save(item.getId(), reqDto);
        String requestBody = new ObjectMapper().writeValueAsString(new DeleteCommentDto(reqDto.getWriter(), reqDto.getPassword()));

        //when
        mockMvc.perform(delete("/items/{itemId}/comments/{commentId}", item.getId(), save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Comment/DELETE/comment 삭제",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(DELETE_COMMENT_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        assertThrows(NoSuchElementException.class, () -> commentRepository.findById(save.getId()).get());
        // 글에서도 사라졌는지 확인한다.
        assertFalse(salesItemRepository.findById(item.getId()).get().getComments().contains(save));
    }

    @BeforeEach
    public void makeItem() {
        String title = "중고 맥북 팝니다";
        String description = "2019년 맥북 프로 13인치 모델입니다";
        int minPriceWanted = 10000;
        String writer = "jeeho.dev";
        String password = "1qaz2wsx";
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted, writer, password));
        item = salesItemRepository.findById(save.getId()).get();
    }

    private static RequestCommentDto getRequestCommentDto() {
        return new RequestCommentDto("jeeho.edu", "qwerty1234", "할인 가능하신가요?");
    }
}
package com.lahee.market.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahee.market.dto.comment.CommentReplyDto;
import com.lahee.market.dto.comment.RequestCommentDto;
import com.lahee.market.dto.comment.ResponseCommentDto;
import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.dto.salesItem.ResponseSalesItemDto;
import com.lahee.market.dto.user.LoginDto;
import com.lahee.market.dto.user.SignupDto;
import com.lahee.market.dto.user.TokenDto;
import com.lahee.market.dto.user.UserResponseDto;
import com.lahee.market.entity.Comment;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.CommentRepository;
import com.lahee.market.repository.SalesItemRepository;
import com.lahee.market.service.CommentService;
import com.lahee.market.service.SalesItemService;
import com.lahee.market.service.UserService;
import jakarta.persistence.EntityManager;
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
    @Autowired
    private UserService userService;

    private SalesItem item;
    private TokenDto itemUserToken, commentUserToken;
    private SignupDto itemUser, commentUser;

    @Test
    @DisplayName("item 생성 조회 (POST /items/{itemId}/comment)")
    public void saveItem() throws Exception {
        //given
        //item 생성
        RequestCommentDto dto = new RequestCommentDto("할인 가능하신가요?");
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        //when
        mockMvc.perform(post("/items/{itemId}/comments", item.getId())
                        .header("Authorization", itemUserToken.getGrantType() + " " + itemUserToken.getAccessToken())
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
        assertThat(comment.getContent()).isEqualTo("할인 가능하신가요?");
    }

    @Test
    @DisplayName("comment 페이징 조회 (GET /items/{itemId}/comments)")
    public void findPagedComment() throws Exception {
        //given
        for (int i = 0; i < 25; i++) {
            commentService.save(item.getId(), new RequestCommentDto("cContent" + i), commentUser.getUsername());
        }

        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}/comments", item.getId())
                        .header("Authorization", commentUserToken.getGrantType() + " " + commentUserToken.getAccessToken())
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
        RequestCommentDto reqDto = new RequestCommentDto("할인 가능하신가요?");
        ResponseCommentDto save = commentService.save(item.getId(), reqDto, commentUser.getUsername());


        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}/comments/{commentId}", item.getId(), save.getId())
                        .header("Authorization", commentUserToken.getGrantType() + " " + commentUserToken.getAccessToken())
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
        RequestCommentDto reqDto = new RequestCommentDto("할인 가능하신가요?");
        ResponseCommentDto savedComment = commentService.save(item.getId(), reqDto, commentUser.getUsername());

        String modifyContent = "[MODIFY] 수정된 댓글";
        RequestCommentDto updateDto = new RequestCommentDto(modifyContent);
        String requestBody = new ObjectMapper().writeValueAsString(updateDto);

        //when
        mockMvc.perform(put("/items/{itemId}/comments/{commentId}", item.getId(), savedComment.getId())
                        .header("Authorization", commentUserToken.getGrantType() + " " + commentUserToken.getAccessToken())
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
        Comment comment = commentRepository.findById(savedComment.getId()).get();
        assertThat(comment.getContent()).isEqualTo(modifyContent);
    }


    @Test
    @DisplayName("comment reply 업데이트 확인 (PUT /items/{itemId}/comments/{commentId})/reply")
    public void updateCommentReply() throws Exception {
        //given
        String reply = "REPLY";
        RequestCommentDto reqDto = new RequestCommentDto("할인 가능하신가요?");
        ResponseCommentDto save = commentService.save(item.getId(), reqDto, commentUser.getUsername());
        CommentReplyDto updateDto = new CommentReplyDto(reply);
        String requestBody = new ObjectMapper().writeValueAsString(updateDto);

        //when
        mockMvc.perform(put("/items/{itemId}/comments/{commentId}/reply", item.getId(), save.getId())
                        .header("Authorization", itemUserToken.getGrantType() + " " + itemUserToken.getAccessToken())
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
        RequestCommentDto reqDto = new RequestCommentDto("할인 가능하신가요?");
        ResponseCommentDto save = commentService.save(item.getId(), reqDto, commentUser.getUsername());

        //when
        mockMvc.perform(delete("/items/{itemId}/comments/{commentId}", item.getId(), save.getId())
                        .header("Authorization", commentUserToken.getGrantType() + " " + commentUserToken.getAccessToken())
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
        itemUser = new SignupDto("Itest", "Itest", "Itest", "Itest");
        commentUser = new SignupDto("Ctest", "Ctest", "Ctest", "Ctest");
        userService.signup(itemUser);
        userService.signup(commentUser);
        itemUserToken = userService.login(new LoginDto(itemUser.getUsername(), itemUser.getPassword()));
        commentUserToken = userService.login(new LoginDto(commentUser.getUsername(), commentUser.getPassword()));


        String title = "중고 맥북 팝니다";
        String description = "2019년 맥북 프로 13인치 모델입니다";
        int minPriceWanted = 10000;
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted), itemUser.getUsername());
        item = salesItemRepository.findById(save.getId()).get();
    }

}
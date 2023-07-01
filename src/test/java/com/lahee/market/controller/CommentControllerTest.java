package com.lahee.market.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahee.market.dto.RequestSalesItemDto;
import com.lahee.market.dto.ResponseSalesItemDto;
import com.lahee.market.dto.comment.RequestCommentDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lahee.market.constants.ControllerMessage.SAVE_COMMENT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("item 생성 조회 (POST /items)")
    public void saveItem() throws Exception {
        //given
        //item 생성
        String cWriter = "jeeho.edu";
        String cPassword = "qwerty1234";
        String cContent = "할인 가능하신가요?";
        RequestCommentDto dto = new RequestCommentDto(cWriter, cPassword, cContent);
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
        assertThat(comment.getWriter()).isEqualTo(cWriter);
        assertThat(comment.getContent()).isEqualTo(cContent);
    }


    @BeforeEach
    public void makeItem() {
        String title = "title";
        String description = "desc";
        int minPriceWanted = 10000;
        String writer = "writer";
        String password = "password";
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted, writer, password));
        item = salesItemRepository.findById(save.getId()).get();
    }
}
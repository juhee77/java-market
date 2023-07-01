package com.lahee.market.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahee.market.dto.DeleteItemDto;
import com.lahee.market.dto.RequestSalesItemDto;
import com.lahee.market.dto.ResponseSalesItemDto;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.SalesItemRepository;
import com.lahee.market.service.SalesItemService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
//데이터 통합 테스트
class SalesItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalesItemRepository salesItemRepository;
    @Autowired
    private SalesItemService salesItemService;


    @Test
    @DisplayName("item 생성 조회 (POST /items)")
    public void saveItem() throws Exception {
        //givne
        String title = "title";
        String description = "desc";
        int minPriceWanted = 10000;
        String writer = "writer";
        String password = "password";
        RequestSalesItemDto dto = new RequestSalesItemDto(title, description, minPriceWanted, writer, password);
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        //when
        mockMvc.perform(post("/items")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        List<SalesItem> all = salesItemRepository.findAll();
        SalesItem salesItem = all.get(0);
        assertThat(salesItem.getWriter()).isEqualTo(writer);
        assertThat(salesItem.getPassword()).isEqualTo(password);
        assertThat(salesItem.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("item 단건 조회 (GET /items/{itemId})")
    public void findOne() throws Exception {
        //givne
        String title = "title";
        String description = "desc";
        int minPriceWanted = 10000;
        String writer = "writer";
        String password = "password";
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted, writer, password));

        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}", save.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        perform.andExpectAll(
                status().is2xxSuccessful(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.id").value(save.getId()),
                jsonPath("$.title").value(save.getTitle()),
                jsonPath("$.description").value(save.getDescription())
        );
    }

    @Test
    @DisplayName("item 페이징 조회 (GET /items)")
    public void findPaged() throws Exception {
        //givne
        for (int i = 0; i < 25; i++) {
            String title = "title" + i;
            String description = "desc" + i;
            int minPriceWanted = 10000;
            String writer = "writer" + i;
            String password = "password" + i;
            salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted, writer, password));
        }

        //when
        ResultActions perform = mockMvc.perform(get("/items")
                .param("page", String.valueOf(2))
                .param("limit", String.valueOf(10))
                .contentType(MediaType.APPLICATION_JSON));

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
    @DisplayName("item 사진 업데이트 조회 (PUT /items/{itemId}/image)")
    @Disabled
    //사진 업로드의 경우 post지원시에  테스트 가능 //TODO 이미지 테스트 경우 해당 파일 제거하여 관리하도록
    public void saveItemPhoto() throws Exception {
        //givne
        String title = "title";
        String description = "desc";
        int minPriceWanted = 10000;
        String writer = "writer";
        String password = "password";
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted, writer, password));


        //when
        mockMvc.perform(multipart("/items/{itemId}/image", save.getId())
                        .file(new MockMultipartFile("image", "image.jpg", "image/jpg", "<<jpg data>>".getBytes(StandardCharsets.UTF_8)))
                        .param("writer", writer)
                        .param("password", password)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        SalesItem salesItem = salesItemRepository.findById(save.getId()).get();
        assertThat(salesItem.getImageUrl()).isEqualTo(String.format("/static/%d/item.jpg", save.getId()));
    }


    @Test
    @DisplayName("item 업데이트 확인 (PUT /items/{itemId})")
    public void updateItem() throws Exception {
        //givne
        int minPriceWanted = 10000;
        String writer = "writer";
        String password = "password";
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto("title", "desc", minPriceWanted, writer, password));
        RequestSalesItemDto updateDto = new RequestSalesItemDto("MODIFY", "MODIFY", minPriceWanted, writer, password);
        String requestBody = new ObjectMapper().writeValueAsString(updateDto);

        //when
        mockMvc.perform(put("/items/{itemId}", save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        SalesItem salesItem = salesItemRepository.findById(save.getId()).get();
        assertThat(salesItem.getTitle()).isEqualTo("MODIFY");
        assertThat(salesItem.getDescription()).isEqualTo("MODIFY");
    }

    @Test
    @DisplayName("item 삭제 확인 (DELETE /items/{itemId})")
    public void deleteItem() throws Exception {
        //givne
        String title = "title";
        String desc = "desc";
        int minPriceWanted = 10000;
        String writer = "writer";
        String password = "password";
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto(title, desc, minPriceWanted, writer, password));
        String requestBody = new ObjectMapper().writeValueAsString(new DeleteItemDto(writer, password));

        //when
        mockMvc.perform(delete("/items/{itemId}", save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        assertThrows(NoSuchElementException.class, () -> salesItemRepository.findById(save.getId()).get());
    }
}
package com.lahee.market.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.dto.salesItem.ResponseSalesItemDto;
import com.lahee.market.dto.user.LoginDto;
import com.lahee.market.dto.user.SignupDto;
import com.lahee.market.dto.user.TokenDto;
import com.lahee.market.dto.user.UserResponseDto;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.SalesItemRepository;
import com.lahee.market.service.SalesItemService;
import com.lahee.market.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static com.lahee.market.constants.ControllerMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@ActiveProfiles("test")
//데이터 통합 테스트
class SalesItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    private SalesItemRepository salesItemRepository;
    @Autowired
    private SalesItemService salesItemService;

    TokenDto loginDto;
    SignupDto signupDto;

    @BeforeEach
    void setUp() {
        signupDto = new SignupDto("test", "test", "test", "test");
        UserResponseDto signup = userService.signup(signupDto);
        loginDto = userService.login(new LoginDto(signup.getUsername(), signupDto.getPassword()));
    }

    @Test
    @DisplayName("item 생성 조회 (POST /items)")
    public void saveItem() throws Exception {
        //given
        RequestSalesItemDto dto = getRequestSalesItemDto();
        String requestSalesItemDto = new ObjectMapper().writeValueAsString(dto);

        //when
        MockMultipartFile jsonFile = new MockMultipartFile("requestSalesItemDto", "requestSalesItemDto", "application/json", requestSalesItemDto.getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart("/items")
                        .file(jsonFile)
                        .header("Authorization", loginDto.getGrantType() + " " + loginDto.getAccessToken()))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Items/POST/item 생성 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(SAVE_ITEM_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        List<SalesItem> all = salesItemRepository.findAll();
        SalesItem salesItem = all.get(0);
        assertThat(salesItem.getTitle()).isEqualTo(dto.getTitle());
    }

    @Test
    @DisplayName("item 단건 조회 (GET /items/{itemId})")
    public void findOne() throws Exception {
        //given
        ResponseSalesItemDto save = salesItemService.save(getRequestSalesItemDto(), signupDto.getUsername());

        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}", save.getId())
                        .header("Authorization", loginDto.getGrantType() + " " + loginDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Items/GET/item 단건 조회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

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
        //given
        for (int i = 0; i < 25; i++) {
            String title = "title" + i;
            String description = "desc" + i;
            int minPriceWanted = 10000 * i;
            salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted), signupDto.getUsername());
        }

        //when
        ResultActions perform = mockMvc.perform(get("/items")
                        .header("Authorization", loginDto.getGrantType() + " " + loginDto.getAccessToken())
                        .param("page", String.valueOf(2))
                        .param("limit", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Items/GET/item 페이징 조회",
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
    @DisplayName("item 사진 업데이트 (PUT /items/{itemId}/image)")
//    @Disabled
    //사진 업로드의 경우 post지원시에  테스트 가능 //TODO 이미지 테스트 경우 해당 파일 제거하여 관리하도록
    public void saveItemPhoto() throws Exception {
        //given
        RequestSalesItemDto reqDto = getRequestSalesItemDto();
        ResponseSalesItemDto save = salesItemService.save(reqDto, signupDto.getUsername());


        //when
        mockMvc.perform(multipart("/items/{itemId}/image", save.getId())
                        .file(new MockMultipartFile("image", "image.jpg", "image/jpg", "<<jpg data>>".getBytes(StandardCharsets.UTF_8)))
                        .header("Authorization", loginDto.getGrantType() + " " + loginDto.getAccessToken())
                        .contentType(MediaType.MULTIPART_FORM_DATA))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Items/PUT/item 사진 업데이트",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(UPDATE_ITEM_IMAGE_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        SalesItem salesItem = salesItemRepository.findById(save.getId()).get();
        assertThat(salesItem.getImageUrl()).isEqualTo(String.format("/static/%d/item.jpg", save.getId()));
    }

    @Test
    @DisplayName("item 업데이트 확인 (PUT /items/{itemId})")
    public void updateItem() throws Exception {
        //given
        String modify = "MODIFY";
        RequestSalesItemDto reqDto = getRequestSalesItemDto();
        ResponseSalesItemDto save = salesItemService.save(reqDto, signupDto.getUsername());
        RequestSalesItemDto updateDto = new RequestSalesItemDto(modify, modify, reqDto.getMinPriceWanted());
        String requestBody = new ObjectMapper().writeValueAsString(updateDto);

        //when
        mockMvc.perform(put("/items/{itemId}", save.getId())
                        .header("Authorization", loginDto.getGrantType() + " " + loginDto.getAccessToken())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Items/PUT/item 업데이트",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(UPDATE_ITEM_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        SalesItem salesItem = salesItemRepository.findById(save.getId()).get();
        assertThat(salesItem.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(salesItem.getDescription()).isEqualTo(updateDto.getDescription());
    }

    @Test
    @DisplayName("item 삭제 확인 (DELETE /items/{itemId})")
    public void deleteItem() throws Exception {
        //given
        RequestSalesItemDto reqDto = getRequestSalesItemDto();
        ResponseSalesItemDto save = salesItemService.save(reqDto, signupDto.getUsername());

        //when
        mockMvc.perform(delete("/items/{itemId}", save.getId())
                        .header("Authorization", loginDto.getGrantType() + " " + loginDto.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Items/DELETE/item 삭제",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(DELETE_ITEM_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        assertThrows(NoSuchElementException.class, () -> salesItemRepository.findById(save.getId()).get());
    }

    private static RequestSalesItemDto getRequestSalesItemDto() {
        String title = "중고 맥북 팝니다";
        String description = "2019년 맥북 프로 13인치 모델입니다";
        int minPriceWanted = 10000;
        return new RequestSalesItemDto(title, description, minPriceWanted);
    }
}
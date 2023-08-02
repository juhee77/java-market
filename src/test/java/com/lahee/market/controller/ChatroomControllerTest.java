package com.lahee.market.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahee.market.constants.ControllerMessage;
import com.lahee.market.dto.chatroom.RequestChatroomDto;
import com.lahee.market.dto.chatroom.ResponseChatroomDto;
import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.dto.salesItem.ResponseSalesItemDto;
import com.lahee.market.dto.user.LoginDto;
import com.lahee.market.dto.user.SignupDto;
import com.lahee.market.dto.user.TokenDto;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.SalesItemRepository;
import com.lahee.market.service.ChatroomService;
import com.lahee.market.service.SalesItemService;
import com.lahee.market.service.UserService;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@ActiveProfiles("test")
class ChatroomControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    private SalesItemService salesItemService;
    @Autowired
    private SalesItemRepository salesItemRepository;
    @Autowired
    private ChatroomService chatroomService;

    private SalesItem item;
    private TokenDto itemUserToken, chatUser1Token, chatUser2Token;
    private SignupDto itemUser, chatUser1, chatUser2;

    @Test
    @DisplayName("chatroom 생성 조회 (POST /chat/rooms)")
    public void saveChatroom() throws Exception {
        //given
        //item 생성
        RequestChatroomDto dto = new RequestChatroomDto(item.getId(), chatUser1.getUsername());
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        //when
        mockMvc.perform(post("/chat/rooms")
                        .header("Authorization", chatUser1Token.getGrantType() + " " + chatUser1Token.getAccessToken())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Chatroom/POST/chatroom 생성",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("itemId").value(item.getId()),
                        jsonPath("roomName").value(chatUser1.getUsername()),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }


    @Test
    @DisplayName("chatroom 입장가능 채팅방 조회 - 판매자 (GET /chat/rooms)")
    public void findChatrooms() throws Exception {
        //given
        chatroomService.createChatRoom(new RequestChatroomDto(item.getId(), chatUser1.getUsername()), chatUser1.getUsername());
        chatroomService.createChatRoom(new RequestChatroomDto(item.getId(), chatUser2.getUsername()), chatUser2.getUsername());

        //when
        ResultActions perform = mockMvc.perform(get("/chat/rooms", item.getId())
                        .header("Authorization", itemUserToken.getGrantType() + " " + itemUserToken.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Chatroom/GET/chatroom 전체(아이템 판매자 - 입장가능한 방들) 확인회",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

        //then
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()", equalTo(2))
        );
    }

    @Test
    @DisplayName("chatroom 입장가능 채팅방 조회 - 제안자(GET /chat/rooms)")
    public void findChatrooms2() throws Exception {
        //given
        chatroomService.createChatRoom(new RequestChatroomDto(item.getId(), chatUser1.getUsername()), chatUser1.getUsername());
        chatroomService.createChatRoom(new RequestChatroomDto(item.getId(), chatUser2.getUsername()), chatUser2.getUsername());

        //when
        ResultActions perform = mockMvc.perform(get("/chat/rooms", item.getId())
                        .header("Authorization", chatUser2Token.getGrantType() + " " + chatUser2Token.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Chatroom/GET/chatroom 전체(아이템 제안자 - 입장가능한 방들) 확인",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

        //then
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()", equalTo(1))
        );
    }

    @Test
    @DisplayName("chatroom 단일 채팅방 조회 (GET /chat/rooms/{roomId})")
    public void findOneChatroom() throws Exception {
        //given
        ResponseChatroomDto chatRoom1 = chatroomService.createChatRoom(new RequestChatroomDto(item.getId(), chatUser1.getUsername()), chatUser1.getUsername());
        ResponseChatroomDto chatRoom2 = chatroomService.createChatRoom(new RequestChatroomDto(item.getId(), chatUser2.getUsername()), chatUser2.getUsername());

        //when
        ResultActions perform = mockMvc.perform(get("/chat/rooms/{roomId}", chatRoom1.getId())
                        .header("Authorization", chatUser1Token.getGrantType() + " " + chatUser1Token.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Chatroom/GET/chatroom 단일 채팅방 확인",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

        //then
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.roomName").value(chatRoom1.getRoomName())
        );
    }

    @Test
    @DisplayName("chatroom 단일 채팅방 삭제 (DELETE /chat/rooms/{roomId})")
    public void deleteOneChatroom() throws Exception {
        //given
        ResponseChatroomDto chatRoom1 = chatroomService.createChatRoom(new RequestChatroomDto(item.getId(), chatUser1.getUsername()), chatUser1.getUsername());

        //when
        ResultActions perform = mockMvc.perform(delete("/chat/rooms/{roomId}", chatRoom1.getId())
                        .header("Authorization", chatUser1Token.getGrantType() + " " + chatUser1Token.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Chatroom/DELETE/chatroom 채팅방 삭제",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

        //then
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("message").value(ControllerMessage.DELETE_CHAT_ROOM)
        );

    }

    @BeforeEach
    public void makeItem() {
        itemUser = new SignupDto("Itest", "Itest", "Itest", "Itest");
        chatUser1 = new SignupDto("Ctest1", "Ctest1", "Ctest1", "Ctest1");
        chatUser2 = new SignupDto("Ctest2", "Ctest2", "Ctest2", "Ctest2");
        userService.signup(itemUser);
        userService.signup(chatUser1);
        userService.signup(chatUser2);
        itemUserToken = userService.login(new LoginDto(itemUser.getUsername(), itemUser.getPassword()));
        chatUser1Token = userService.login(new LoginDto(chatUser1.getUsername(), chatUser1.getPassword()));
        chatUser2Token = userService.login(new LoginDto(chatUser2.getUsername(), chatUser2.getPassword()));


        String title = "중고 맥북 팝니다";
        String description = "2019년 맥북 프로 13인치 모델입니다";
        int minPriceWanted = 10000;
        ResponseSalesItemDto save = salesItemService.save(new RequestSalesItemDto(title, description, minPriceWanted), itemUser.getUsername());
        item = salesItemRepository.findById(save.getId()).get();
    }
}
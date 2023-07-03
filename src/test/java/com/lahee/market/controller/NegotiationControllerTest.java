package com.lahee.market.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lahee.market.dto.RequestSalesItemDto;
import com.lahee.market.dto.ResponseSalesItemDto;
import com.lahee.market.dto.comment.DeleteCommentDto;
import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.NegotiationRepository;
import com.lahee.market.repository.SalesItemRepository;
import com.lahee.market.service.NegotiationService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
class NegotiationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SalesItemRepository salesItemRepository;
    @Autowired
    private NegotiationRepository negotiationRepository;
    @Autowired
    private SalesItemService salesItemService;
    @Autowired
    private NegotiationService negotiationService;

    private static SalesItem item;

    @Test
    @DisplayName("proposal 생성 조회 (POST /items/{itemId}/proposal)")
    public void saveProposal() throws Exception {
        //given
        //item 생성
        String pWriter = "jeeho.edu";
        String pPassword = "qwerty1234";
        Integer pSuggestedPrice = 100000;
        RequestNegotiationDto dto = new RequestNegotiationDto(pWriter, pPassword, pSuggestedPrice);
        String requestBody = new ObjectMapper().writeValueAsString(dto);

        //when
        mockMvc.perform(post("/items/{itemId}/proposal", item.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Negotiation/POST/negotiation 생성",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(SAVE_PROPOSAL_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        List<Negotiation> all = negotiationRepository.findAll();
        Negotiation negotiation = all.get(0);
        assertThat(negotiation.getSalesItem().getId()).isEqualTo(item.getId());
        assertThat(negotiation.getWriter()).isEqualTo(pWriter);
        assertThat(negotiation.getSuggestedPrice()).isEqualTo(pSuggestedPrice);
    }

    @Test
    @DisplayName("proposal 페이징 조회 - 판매자 (GET /items/{itemId}/proposal)")
    public void findPagedProposal() throws Exception {
        //given
        for (int i = 0; i < 10; i++) {
            negotiationService.save(item.getId(), new RequestNegotiationDto("pWriter" + i, "pPassword" + i, 10000));
        }

        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}/proposal", item.getId())
                        .param("writer", item.getWriter())
                        .param("password", item.getPassword())
                        .param("page", String.valueOf(0))
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Proposal/GET/proposal 페이징 조회 - 판매자의 경우",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));
        //then
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("totalElements", equalTo(10)),
                jsonPath("totalPages", equalTo(1)),
                jsonPath("numberOfElements", equalTo(10))
        );
    }

    @Test
    @DisplayName("proposal 페이징 조회 - 제안 작성자 (GET /items/{itemId}/proposal)")
    public void findPagedProposal2() throws Exception {
        //given
        for (int i = 0; i < 5; i++) {
            negotiationService.save(item.getId(), new RequestNegotiationDto("pWriter", "pPassword", i * 1000));
        }
        for (int i = 0; i < 5; i++) {
            negotiationService.save(item.getId(), new RequestNegotiationDto("p2Writer" + i, "p2Password" + i, 20000));
        }

        //when
        ResultActions perform = mockMvc.perform(get("/items/{itemId}/proposal", item.getId())
                        .param("writer", "pWriter")
                        .param("password", "pPassword")
                        .param("page", String.valueOf(0))
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Proposal/GET/proposal 페이징 조회 - 제안 작성자의 경우",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())));

        //then
        perform.andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("totalElements", equalTo(5)),
                jsonPath("totalPages", equalTo(1)),
                jsonPath("numberOfElements", equalTo(5))
        );
    }


    @Test
    @DisplayName("proposal 업데이트 확인 (PUT /items/{itemId}/proposal/{proposalId})")
    public void updateProposal() throws Exception {
        //given
        ResponseNegotiationDto save = negotiationService.save(item.getId(), new RequestNegotiationDto("cWriter", "cPassword", 1000));
        Map<String, String> requestDto = new HashMap<>();
        requestDto.put("writer", "cWriter");
        requestDto.put("password", "cPassword");
        requestDto.put("suggestedPrice", "50000");
        String requestBody = new ObjectMapper().writeValueAsString(requestDto);

        //when
        mockMvc.perform(put("/items/{itemId}/proposal/{proposalId}", item.getId(), save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Proposal/PUT/proposal 업데이트",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(UPDATE_PROPOSAL_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        Negotiation negotiation = negotiationRepository.findById(save.getId()).get();
        assertThat(negotiation.getSuggestedPrice()).isEqualTo(Integer.parseInt(requestDto.get("suggestedPrice")));
    }

    @Test
    @DisplayName("proposal 상태 업데이트 확인 - 판매자가 제안을 수락하는 (PUT /items/{itemId}/proposal/{proposalId})")
    public void updateStatusProposal() throws Exception {
        //given
        ResponseNegotiationDto save = negotiationService.save(item.getId(), new RequestNegotiationDto("pWriter", "pPassword", 1000));
        Map<String, String> requestDto = new HashMap<>();
        requestDto.put("writer", item.getWriter());
        requestDto.put("password", item.getPassword());
        requestDto.put("status", "수락");
        String requestBody = new ObjectMapper().writeValueAsString(requestDto);

        //when
        mockMvc.perform(put("/items/{itemId}/proposal/{proposalId}", item.getId(), save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Proposal/PUT/proposal 상태(수락,거절) 업데이트",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(UPDATE_PROPOSAL_STATUS_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        Negotiation negotiation = negotiationRepository.findById(save.getId()).get();
        assertThat(negotiation.getStatus().getName()).isEqualTo(requestDto.get("status"));
    }

    @Test
    @DisplayName("proposal 상태 업데이트 확인 - 제안자가 확정하는 (PUT /items/{itemId}/proposal/{proposalId})")
    public void confirmationProposal() throws Exception {
        //given
        RequestNegotiationDto dto = new RequestNegotiationDto("pWriter", "pPassword", 1000);
        ResponseNegotiationDto save = negotiationService.save(item.getId(), dto);
        negotiationService.updateStatus(item.getId(),save.getId(),new UpdateNegotiationDto(item.getWriter(),item.getPassword(),save.getSuggestedPrice(),"수락"));
        Map<String, String> requestDto = new HashMap<>();
        requestDto.put("writer", dto.getWriter());
        requestDto.put("password", dto.getPassword());
        requestDto.put("status", "확정");
        String requestBody = new ObjectMapper().writeValueAsString(requestDto);

        //when
        mockMvc.perform(put("/items/{itemId}/proposal/{proposalId}", item.getId(), save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("Proposal/PUT/proposal 상태(확정) 업데이트",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(CONFIRMATION_PROPOSAL_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        Negotiation negotiation = negotiationRepository.findById(save.getId()).get();
        assertThat(negotiation.getStatus().getName()).isEqualTo(requestDto.get("status"));
    }



    @Test
    @DisplayName("proposal 삭제 확인 (DELETE /items/{itemId}/proposal/{proposalId})")
    public void deleteComment() throws Exception {
        //given
        RequestNegotiationDto dto = new RequestNegotiationDto("pWriter", "pPassword", 10000);
        ResponseNegotiationDto save = negotiationService.save(item.getId(), dto);
        String requestBody = new ObjectMapper().writeValueAsString(new DeleteCommentDto(dto.getWriter(), dto.getPassword()));

        //when
        mockMvc.perform(delete("/items/{itemId}/proposal/{proposalId}", item.getId(), save.getId())
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("proposal/DELETE/proposal 삭제",
                        Preprocessors.preprocessRequest(prettyPrint()),
                        Preprocessors.preprocessResponse(prettyPrint())))

                .andExpectAll(
                        status().is2xxSuccessful(),
                        jsonPath("message").value(DELETE_PROPOSAL_MESSAGE),
                        content().contentType(MediaType.APPLICATION_JSON)
                );

        //then
        assertThrows(NoSuchElementException.class, () -> negotiationRepository.findById(save.getId()).get());
        // 글에서도 사라졌는지 확인한다.
        assertFalse(salesItemRepository.findById(item.getId()).get().getNegotiations().contains(save));
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
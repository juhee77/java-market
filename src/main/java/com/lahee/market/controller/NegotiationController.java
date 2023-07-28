package com.lahee.market.controller;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.exception.CustomException;
import com.lahee.market.exception.ErrorCode;
import com.lahee.market.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lahee.market.constants.ControllerMessage.*;
import static com.lahee.market.dto.ResponseDto.getInstance;
import static com.lahee.market.util.SecurityUtil.getCurrentUsername;

@RestController
@RequestMapping("/items/{itemId}/proposal")
@RequiredArgsConstructor
@Slf4j
public class NegotiationController {
    private final NegotiationService negotiationService;

    @PostMapping
    public ResponseEntity<ResponseDto> saveProposal(@PathVariable("itemId") Long itemId, @Valid @RequestBody RequestNegotiationDto dto) {
        negotiationService.save(itemId, dto, getCurrentUsername());
        return ResponseEntity.ok(getInstance(SAVE_PROPOSAL_MESSAGE));
    }

    @GetMapping
    public ResponseEntity<Page<ResponseNegotiationDto>> findAllProposalByItem(
            @PathVariable("itemId") Long itemId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(negotiationService.findAllEntityByItem(itemId, page, limit, getCurrentUsername()));
    }

    @PutMapping(value = "/{proposalId}")
    public ResponseEntity<ResponseDto> updateProposal(
            @PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId,
            @Valid @RequestBody UpdateNegotiationDto requestNegotiationDto) {
        //status, price 가격이 둘다 없는 경우(잘못된 요청)
        if (requestNegotiationDto.getStatus() == null && requestNegotiationDto.getSuggestedPrice() == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        if (requestNegotiationDto.getStatus() == null) { //제안 가격 수정의 경우
            negotiationService.update(itemId, proposalId, requestNegotiationDto, getCurrentUsername());
            return ResponseEntity.ok(getInstance(UPDATE_PROPOSAL_MESSAGE));
        } else if (requestNegotiationDto.getStatus().equals("확정")) {
            //제안자가 확정을 하는 경우
            negotiationService.acceptProposal(itemId, proposalId, getCurrentUsername());
            return ResponseEntity.ok(getInstance(CONFIRMATION_PROPOSAL_MESSAGE));
        } else { // 수락, 거절중 하나이다. (status는 null, 수락, 거절, 확정 중 하나이다)
            negotiationService.updateStatus(itemId, proposalId, requestNegotiationDto, getCurrentUsername());
            return ResponseEntity.ok(getInstance(UPDATE_PROPOSAL_STATUS_MESSAGE));
        }
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> deleteProposal(
            @PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId) {
        negotiationService.delete(itemId, proposalId, getCurrentUsername());
        return ResponseEntity.ok(getInstance(DELETE_PROPOSAL_MESSAGE));
    }
}

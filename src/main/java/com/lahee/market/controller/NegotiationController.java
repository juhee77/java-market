package com.lahee.market.controller;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.negotiation.DeleteNegotiationDto;
import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lahee.market.constants.ControllerMessage.*;

@RestController
@RequestMapping("/items/{itemId}/proposal")
@RequiredArgsConstructor
@Slf4j
public class NegotiationController {
    private final NegotiationService negotiationService;

    @PostMapping
    public ResponseEntity<ResponseDto> saveProposal(@PathVariable("itemId") Long itemId, @Valid @RequestBody RequestNegotiationDto dto) {
        negotiationService.save(itemId, dto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance(SAVE_PROPOSAL_MESSAGE));
    }


    @GetMapping
    public ResponseEntity<Page<ResponseNegotiationDto>> findAllProposalByItem(
            @PathVariable("itemId") Long itemId,
            @RequestParam("writer") String writer,
            @RequestParam("password") String password,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(negotiationService.findAllEntityByItem(itemId, writer, password, page, limit));
    }

    @PutMapping(value = "/{proposalId}")
    public ResponseEntity<ResponseDto> updateProposal(
            @PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId,
            @RequestBody UpdateNegotiationDto requestNegotiationDto) {
        if (requestNegotiationDto.getStatus() == null) { //제안 가격 수정의 경우
            negotiationService.update(itemId, proposalId, requestNegotiationDto);
            return ResponseEntity.ok(ResponseDto.getSuccessInstance(UPDATE_PROPOSAL_MESSAGE));
        } else if (requestNegotiationDto.getStatus().equals("확정")) {
            //제안자가 확정을 하는 경우
            negotiationService.acceptProposal(itemId, proposalId, requestNegotiationDto);
            return ResponseEntity.ok(ResponseDto.getSuccessInstance(CONFIRMATION_PROPOSAL_MESSAGE));
        } else if (requestNegotiationDto.getStatus().equals("수락") || requestNegotiationDto.getStatus().equals("거절")) {
            //판매자가 수락, 거부 결정을 하는 경우
            negotiationService.updateStatus(itemId, proposalId, requestNegotiationDto);
            return ResponseEntity.ok(ResponseDto.getSuccessInstance(UPDATE_PROPOSAL_STATUS_MESSAGE));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.getSuccessInstance("잚못된 요청입니다..(주로 뭐.. 거절.. 수락,, 확정 틀린게 아닐까용..)"));
    }

//    @PutMapping(value = "/{proposalId}")
//    public ResponseEntity<ResponseDto> updateStatusProposal(
//            @PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId,
//            @Valid @RequestBody UpdateNegotiationStatusDto updateNegotiationStatusDto) {
//        negotiationService.updateStatus(itemId, proposalId, updateNegotiationStatusDto);
//        return ResponseEntity.ok(ResponseDto.getSuccessInstance(UPDATE_PROPOSAL_STATUS_MESSAGE));
//    }


//    @PutMapping(value = "/{proposalId}", params = "itemId,proposalId")
//    public ResponseEntity<ResponseDto> updateProposal(
//            @PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId,
//            @RequestBody RequestNegotiationDto requestNegotiationDto) {
//        negotiationService.update(itemId, proposalId, requestNegotiationDto);
//        return ResponseEntity.ok(ResponseDto.getSuccessInstance(UPDATE_PROPOSAL_MESSAGE));
//    }
//
//    @PutMapping(value = "/{proposalId}")
//    public ResponseEntity<ResponseDto> updateStatusProposal(
//            @PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId,
//            @Valid @RequestBody UpdateNegotiationStatusDto updateNegotiationStatusDto) {
//        negotiationService.updateStatus(itemId, proposalId, updateNegotiationStatusDto);
//        return ResponseEntity.ok(ResponseDto.getSuccessInstance(UPDATE_PROPOSAL_STATUS_MESSAGE));
//    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity<ResponseDto> deleteProposal(
            @PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId,
            @RequestBody DeleteNegotiationDto dto) {
        negotiationService.delete(itemId, proposalId, dto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance(DELETE_PROPOSAL_MESSAGE));
    }
}

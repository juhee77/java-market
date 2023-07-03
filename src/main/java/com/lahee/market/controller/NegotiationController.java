package com.lahee.market.controller;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lahee.market.constants.ControllerMessage.SAVE_PROPOSAL_MESSAGE;

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
}

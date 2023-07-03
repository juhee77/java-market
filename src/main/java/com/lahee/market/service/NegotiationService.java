package com.lahee.market.service;

import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.ItemNotFoundException;
import com.lahee.market.repository.NegotiationRepository;
import com.lahee.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NegotiationService {
    private final SalesItemRepository salesItemRepository;
    private final NegotiationRepository negotiationRepository;

    @Transactional
    public ResponseNegotiationDto save(Long itemId, RequestNegotiationDto dto) {
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Negotiation saved = Negotiation.getEntityInstance(dto);
        saved.setSalesItem(salesItem);
        return ResponseNegotiationDto.fromEntity(negotiationRepository.save(saved));
    }

}

package com.lahee.market.service;

import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.entity.*;
import com.lahee.market.exception.CustomException;
import com.lahee.market.exception.ErrorCode;
import com.lahee.market.repository.NegotiationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.lahee.market.dto.negotiation.ResponseNegotiationDto.fromEntity;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NegotiationService {
    private final SalesItemService salesItemService;
    private final NegotiationRepository negotiationRepository;
    private final UserService userService;

    @Transactional
    public ResponseNegotiationDto save(Long itemId, RequestNegotiationDto dto, String username) {
        User user = userService.getUser(username);
        SalesItem salesItem = salesItemService.getSalesItem(itemId);
        //아이템이 현재 판매중인 상품인지 확인(판매 완료 되었으면 판매하지 않는다)
        if (!(salesItem.getStatus() == ItemStatus.SALE)) {
            throw new CustomException(ErrorCode.ITEM_SOLD_OUT);
        }
        Negotiation saved = Negotiation.getEntityInstance(dto, salesItem, user);

        return fromEntity(negotiationRepository.save(saved));
    }

    public Page<ResponseNegotiationDto> findAllEntityByItem(Long itemId, Integer page, Integer limit, String username) {
        SalesItem salesItem = salesItemService.getSalesItem(itemId);
        User user = userService.getUser(username);
        Pageable pageable = PageRequest.of(page, limit);
        if (salesItem.getUser() == user) {
            //판매자인 경우 모두 확인가능
            return negotiationRepository.findBySalesItem(salesItem, pageable).map(ResponseNegotiationDto::fromEntity);
        } else {
            //판매자가 아니라면 해당유저가 작성한 제안을 반환한다.
            return negotiationRepository.findByUser(user, pageable).map(ResponseNegotiationDto::fromEntity);
        }
    }

    @Transactional
    public ResponseNegotiationDto update(Long itemId, Long proposalId, UpdateNegotiationDto dto, String username) {
        User user = userService.getUser(username);
        Negotiation negotiation = getNegotiation(proposalId);

        negotiation.validItemIdInURL(itemId); //아이템에 속한 제안이 맞는지 확인한다.
        negotiation.validNegotiationUser(user);//해당 유저가 작성한 제안이 맞는지 확인한다.
        negotiation.update(dto);
        return fromEntity(negotiation);
    }

    @Transactional
    public void delete(Long itemId, Long proposalId, String username) {
        User user = userService.getUser(username);
        Negotiation negotiation = getNegotiation(proposalId);

        negotiation.validItemIdInURL(itemId);
        negotiation.validNegotiationUser(user);//해당 유저가 작성한 제안이 맞는지 확인한다.
        negotiation.delete(); //연관관계 내에서도 제거한다.
        negotiationRepository.delete(negotiation);
    }

    @Transactional
    public void updateStatus(Long itemId, Long proposalId, UpdateNegotiationDto dto, String username) { //아이템 판매자가 하는 결정
        User user = userService.getUser(username);
        Negotiation negotiation = getNegotiation(proposalId);

        negotiation.validItemIdInURL(itemId);
        negotiation.validItemUser(user);
        negotiation.updateStatus(NegotiationStatus.findNegotiationStatus(dto.getStatus()));
    }

    @Transactional
    public void acceptProposal(Long itemId, Long proposalId, String username) { //제안자가 확정하는 결정
        User user = userService.getUser(username);
        Negotiation negotiation = getNegotiation(proposalId);
        negotiation.validItemIdInURL(itemId);
        negotiation.validNegotiationUser(user);

        //수락 상태인 경우만 진행한다.
        if (negotiation.getStatus() != NegotiationStatus.ACCEPT) {
            throw new CustomException(ErrorCode.NEGOTIATION_INVALID_STATUS_EXCEPTION);
        }

        SalesItem salesItem = salesItemService.getSalesItem(itemId);
        negotiation.acceptStatus(); //구매 제안의 상태를 확정으로변경하고
        salesItem.updateSoldOutStatus(); //물품의 상태를 판매 완료로변경한다.
        //해당 아이템의 나머지 제안들을 REJECT 으로 바꾼다.
        negotiationRepository.updateItemStatusToReject(salesItem, proposalId);
    }


    public Negotiation getNegotiation(Long negotiationId) {
        Optional<Negotiation> negotiation = negotiationRepository.findById(negotiationId);
        if (negotiation.isEmpty()) {
            throw new CustomException(ErrorCode.NEGOTIATION_NOT_FOUND_EXCEPTION);
        }
        return negotiation.get();
    }
}

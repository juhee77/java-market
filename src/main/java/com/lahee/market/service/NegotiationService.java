package com.lahee.market.service;

import com.lahee.market.dto.negotiation.DeleteNegotiationDto;
import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.NegotiationStatus;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.ItemNotFoundException;
import com.lahee.market.exception.NegotiationInvalidStatusException;
import com.lahee.market.exception.NegotiationNotFoundException;
import com.lahee.market.exception.NegotiationNotMatchItemException;
import com.lahee.market.repository.NegotiationRepository;
import com.lahee.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lahee.market.dto.negotiation.ResponseNegotiationDto.fromEntity;

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
        saved.setSalesItem(salesItem); //연관관계 매핑
        return fromEntity(negotiationRepository.save(saved));
    }

    public Page<ResponseNegotiationDto> findAllEntityByItem(Long itemId, String writer, String password, Integer page, Integer limit) {
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Pageable pageable = PageRequest.of(page, limit);
        if (salesItem.getWriter().equals(writer) && salesItem.getPassword().equals(password)) {
            //판매자인 경우 모두 확인가능하다.
            return negotiationRepository.findBySalesItem(salesItem, pageable).map(ResponseNegotiationDto::fromEntity);
        } else {
            //제안중에 아이템과 파라미터의 작성자 비밀번호가 일치하는 경우를 찾는다.
            Page<Negotiation> negotiations = negotiationRepository.findBySalesItemAndWriterAndPassword(salesItem, writer, password, pageable);
            return negotiations.map(ResponseNegotiationDto::fromEntity);
        }
    }

    @Transactional
    public ResponseNegotiationDto update(Long itemId, Long proposalId, UpdateNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotMatchItemException::new);
        negotiation.validItemIdInURL(itemId); //아이템에 속한 제안이 맞는지 확인한다.
        negotiation.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());

        negotiation.update(dto);
        return fromEntity(negotiation);
    }

    @Transactional
    public void delete(Long itemId, Long proposalId, DeleteNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotMatchItemException::new);
        negotiation.validItemIdInURL(itemId);
        negotiation.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());

        negotiation.getSalesItem().deleteNegotiation(negotiation); //연관관계 내에서도 제거한다.
        negotiationRepository.delete(negotiation);
    }

    @Transactional
    public void updateStatus(Long itemId, Long proposalId, UpdateNegotiationDto dto) { //아이템 판매자가 하는 결정
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotFoundException::new);
        negotiation.validItemIdInURL(itemId);

        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        salesItem.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());//판매자의 아이디 비밀번호 확인

        negotiation.updateStatus(NegotiationStatus.findNegotiationStatus(dto.getStatus()));
    }

    @Transactional
    public void acceptProposal(Long itemId, Long proposalId, UpdateNegotiationDto dto) { //제안자가 확정하는 결정
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotFoundException::new);
        negotiation.validItemIdInURL(itemId);
        negotiation.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());//제안자의 아이디 비밀번호 확인

        //수락 상태인 경우만 진행한다.
        if (negotiation.getStatus() != NegotiationStatus.ACCEPT) {
            throw new NegotiationInvalidStatusException();
        }

        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        negotiation.acceptStatus(); //구매 제안의 상태를 확정으로변경하고
        salesItem.updateSoldOutStatus(); //물품의 상태를 판매 완료로변경한다.
        //해당 아이템의 나머지 제안들을 REJECT 으로 바꾼다.
        negotiationRepository.updateItemStatusToReject(salesItem, proposalId);
    }
}

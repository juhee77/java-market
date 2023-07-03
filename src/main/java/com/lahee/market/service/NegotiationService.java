package com.lahee.market.service;

import com.lahee.market.dto.negotiation.DeleteNegotiationDto;
import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.NegotiationStatus;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.ItemNotFoundException;
import com.lahee.market.exception.NegotationNotMatchItemException;
import com.lahee.market.exception.NegotiationNotFoundException;
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
        saved.setSalesItem(salesItem);
        return fromEntity(negotiationRepository.save(saved));
    }

    public Page<ResponseNegotiationDto> findAllEntityByItem(Long itemId, String writer, String password, Integer page, Integer limit) {
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Pageable pageable = PageRequest.of(page, limit);
        if (salesItem.getWriter().equals(writer) && salesItem.getPassword().equals(password)) {
            //판매자인 경우 모두 확인가능하다.
            return negotiationRepository.findBySalesItem(salesItem, pageable).map(ResponseNegotiationDto::fromEntity);
        } else {
            //작성자중에 아이템과 파라미터의 작성자 비밀번호가 일치하는 경우를 찾는다.
            return negotiationRepository.findBySalesItemAndWriterAndPassword(salesItem, writer, password, pageable).map(ResponseNegotiationDto::fromEntity);
        }
    }

    @Transactional
    public ResponseNegotiationDto update(Long itemId, Long proposalId, UpdateNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotationNotMatchItemException::new);
        validItemNegotiation(itemId, negotiation);
        negotiation.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());
        negotiation.update(dto);
        return fromEntity(negotiation);
    }

    @Transactional
    public void delete(Long itemId, Long proposalId, DeleteNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotationNotMatchItemException::new);
        validItemNegotiation(itemId, negotiation);
        negotiation.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());
        negotiationRepository.delete(negotiation);
    }

    @Transactional
    public void updateStatus(Long itemId, Long proposalId, UpdateNegotiationDto dto) { //아이템 판매자가 하는 결정
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotFoundException::new);
        validItemNegotiation(itemId, negotiation);
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

        //판매자의 아이디 비밀번호 확인
        salesItem.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());
        negotiation.updateStatus(NegotiationStatus.findNegotiationStatus(dto.getStatus()));
    }

    @Transactional
    public void acceptProposal(Long itemId, Long proposalId, UpdateNegotiationDto dto) { //제안자가 확정하는 결정
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotFoundException::new);
        validItemNegotiation(itemId, negotiation);

        //제안자의 아이디 비밀번호 확인
        negotiation.checkAuthAndThrowException(dto.getWriter(), dto.getPassword());

        //수락 상태인 경우만 진행한다.
        if (negotiation.getStatus() != NegotiationStatus.ACCEPT) {
            throw new RuntimeException("수락 상태가 아니다 .");
        }

        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        negotiation.acceptStatus(); //구매 제안의 상태를 확정으로
        salesItem.updateSoldOutStatus(); //물품의 상태를 판매 완료로
        //해당 아이템의 확정되지 않은 나머지 제안들을 REJECT 으로 바꾼다.
        negotiationRepository.updateItemStatusToReject(salesItem);
    }

    private static void validItemNegotiation(Long itemId, Negotiation negotiation) {
        if (itemId != negotiation.getSalesItem().getId()) {
            throw new NegotationNotMatchItemException();
        }
    }

}

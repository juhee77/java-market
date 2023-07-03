package com.lahee.market.service;

import com.lahee.market.dto.negotiation.DeleteNegotiationDto;
import com.lahee.market.dto.negotiation.RequestNegotiationDto;
import com.lahee.market.dto.negotiation.ResponseNegotiationDto;
import com.lahee.market.dto.negotiation.UpdateNegotiationDto;
import com.lahee.market.entity.Negotiation;
import com.lahee.market.entity.NegotiationStatus;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.exception.*;
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
        //판매자인 경우 모두 확인가능하다.
        if (checkItemWriterAndPassword(writer, password, salesItem)) {
            return negotiationRepository.findBySalesItem(salesItem, pageable).map(ResponseNegotiationDto::fromEntity);
        } else {
            //작성자중에 아이템과 파라미터의 작성자 비밀번호가 일치하는 경우를 찾는다.
            return negotiationRepository.findBySalesItemAndWriterAndPassword(salesItem, writer, password, pageable).map(ResponseNegotiationDto::fromEntity);
        }
    }


    @Transactional
    public ResponseNegotiationDto update(Long itemId, Long proposalId, UpdateNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotationNotMatchItemException::new);

        if (negotiation.getSalesItem().getId() != itemId) {
            throw new NegotationNotMatchItemException();
        }

        checkWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), negotiation);
        negotiation.update(dto);
        return fromEntity(negotiation);
    }

    @Transactional
    public void delete(Long itemId, Long proposalId, DeleteNegotiationDto dto) {
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotationNotMatchItemException::new);
        if (negotiation.getSalesItem().getId() != itemId) {
            throw new NegotationNotMatchItemException();
        }

        checkWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), negotiation);
        negotiationRepository.delete(negotiation);
    }

//    public void updateStatus(Long itemId, Long proposalId, UpdateNegotiationStatusDto dto) {
//        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
//        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotFoundException::new);
//
//        checkWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), salesItem);
//        if(salesItem.getId() != negotiation.getSalesItem().getId()) throw new NegotationNotMatchItemException();
//
//        negotiation.updateStatus(dto);
//    }

    @Transactional
    public void updateStatus(Long itemId, Long proposalId, UpdateNegotiationDto dto) { //아이템 판매자가 하는 결정
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotFoundException::new);

        checkWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), salesItem);
        if (salesItem.getId() != negotiation.getSalesItem().getId()) throw new NegotationNotMatchItemException();

        negotiation.updateStatus(dto);
    }

    @Transactional
    public void acceptProposal(Long itemId, Long proposalId, UpdateNegotiationDto dto) { //제안자가 확정하는 결정
        SalesItem salesItem = salesItemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        Negotiation negotiation = negotiationRepository.findById(proposalId).orElseThrow(NegotiationNotFoundException::new);

        checkWriterAndPasswordAndThrowException(dto.getWriter(), dto.getPassword(), negotiation);
        if (salesItem.getId() != negotiation.getSalesItem().getId()) throw new NegotationNotMatchItemException();
        if (negotiation.getStatus() != NegotiationStatus.ACCEPT) {
            throw new RuntimeException("수락 상태가 아니다 .");
        }
        negotiation.acceptStatus(dto);
    }

    private boolean checkItemWriterAndPassword(String writer, String password, SalesItem item) {
        return item.getWriter().equals(writer) && item.getPassword().equals(password);
    }

    private static void checkWriterAndPasswordAndThrowException(String writer, String password, Negotiation negotiation) {
        if (!negotiation.getWriter().equals(writer)) {
            throw new WriterNameNotMatchException();
        }
        if (!negotiation.getPassword().equals(password)) {
            throw new PasswordNotMatchException();
        }
    }

    private static void checkWriterAndPasswordAndThrowException(String writer, String password, SalesItem negotiation) {
        if (!negotiation.getWriter().equals(writer)) {
            throw new WriterNameNotMatchException();
        }
        if (!negotiation.getPassword().equals(password)) {
            throw new PasswordNotMatchException();
        }
    }
}

package com.lahee.market.service;

import com.lahee.market.dto.RequsetSalesItemDto;
import com.lahee.market.dto.ResponseSalesItemDto;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesItemService {
    private final SalesItemRepository salesItemRepository;


    public void save(RequsetSalesItemDto requsetSalesItemDto) {
        salesItemRepository.save(SalesItem.postNewItem(requsetSalesItemDto));
    }

    public Page<ResponseSalesItemDto> readItemPaged(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id"));
        Page<SalesItem> articleEntityPage = salesItemRepository.findAll(pageable);

        Page<ResponseSalesItemDto> articleDtoPage = articleEntityPage.map(ResponseSalesItemDto::fromEntity);
        return articleDtoPage;
    }

    public ResponseSalesItemDto readOneItem(Long id) {
        SalesItem item = salesItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseSalesItemDto.fromEntity(item);
    }
}

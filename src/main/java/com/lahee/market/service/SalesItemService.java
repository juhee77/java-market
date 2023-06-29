package com.lahee.market.service;

import com.lahee.market.dto.ItemDto;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesItemService {
    private final SalesItemRepository salesItemRepository;


    public void save(ItemDto itemDto) {
        salesItemRepository.save(SalesItem.postNewItem(itemDto));
    }
}

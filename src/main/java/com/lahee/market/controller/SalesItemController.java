package com.lahee.market.controller;

import com.lahee.market.dto.RequsetSalesItemDto;
import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.ResponseSalesItemDto;
import com.lahee.market.service.SalesItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class SalesItemController {
    private final SalesItemService salesItemService;

    @PostMapping
    public ResponseEntity<ResponseDto> saveItem(@RequestBody @Valid RequsetSalesItemDto requsetSalesItemDto) {
        salesItemService.save(requsetSalesItemDto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance());
    }

    @GetMapping
    public ResponseEntity<Page<ResponseSalesItemDto>> getAllItemWithPaged(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(salesItemService.readItemPaged(page, limit));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ResponseSalesItemDto> getOneItem(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(salesItemService.readOneItem(itemId));
    }
}

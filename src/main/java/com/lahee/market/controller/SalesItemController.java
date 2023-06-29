package com.lahee.market.controller;

import com.lahee.market.dto.ItemDto;
import com.lahee.market.dto.ResponseDto;
import com.lahee.market.service.SalesItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class SalesItemController {
    private final SalesItemService salesItemService;

    @PostMapping
    public ResponseEntity<ResponseDto> saveItem(@RequestBody @Valid ItemDto itemDto) {
        salesItemService.save(itemDto);
        return ResponseEntity.ok(ResponseDto.getSuccessInstance());
    }

}

package com.lahee.market.controller;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.dto.salesItem.ResponseSalesItemDto;
import com.lahee.market.service.SalesItemService;
import com.lahee.market.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.lahee.market.constants.ControllerMessage.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class SalesItemController {
    private final SalesItemService salesItemService;

    @PostMapping
    public ResponseEntity<ResponseDto> saveItem(@RequestBody @Valid RequestSalesItemDto requestSalesItemDto) {
        salesItemService.save(requestSalesItemDto, SecurityUtil.getCurrentUsername());
        return ResponseEntity.ok(ResponseDto.getInstance(SAVE_ITEM_MESSAGE));
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

    @PutMapping("/{itemId}")
    public ResponseEntity<ResponseDto> updateItem(
            @PathVariable("itemId") Long itemId, @RequestBody @Valid RequestSalesItemDto requestSalesItemDto) {
        salesItemService.update(itemId, requestSalesItemDto);
        return ResponseEntity.ok(ResponseDto.getInstance(UPDATE_ITEM_MESSAGE));
    }

    @RequestMapping(value = "/{itemId}/image", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<ResponseDto> saveItemImage(
            @PathVariable("itemId") Long itemId, @RequestParam("image") MultipartFile image) {
        salesItemService.saveItemImage(itemId, image);
        return ResponseEntity.ok(ResponseDto.getInstance(UPDATE_ITEM_IMAGE_MESSAGE));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ResponseDto> deleteItem(@PathVariable("itemId") Long itemId) {
        salesItemService.deleteItem(itemId);
        return ResponseEntity.ok(ResponseDto.getInstance(DELETE_ITEM_MESSAGE));
    }
}

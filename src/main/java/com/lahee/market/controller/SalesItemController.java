package com.lahee.market.controller;

import com.lahee.market.dto.ApiResponse;
import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.dto.salesItem.ResponseSalesItemDto;
import com.lahee.market.service.SalesItemService;
import com.lahee.market.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.lahee.market.constants.ControllerMessage.*;
import static com.lahee.market.dto.ResponseDto.getInstance;
import static com.lahee.market.util.SecurityUtil.getCurrentUsername;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class SalesItemController {
    private final SalesItemService salesItemService;

    @PostMapping
    public ApiResponse<ResponseDto> saveItem(@RequestBody @Valid RequestSalesItemDto requestSalesItemDto) {
        salesItemService.save(requestSalesItemDto, SecurityUtil.getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK,getInstance(SAVE_ITEM_MESSAGE));
    }

    @GetMapping
    public ApiResponse<Page<ResponseSalesItemDto>> getAllItemWithPaged(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit) {
        return new ApiResponse<>(HttpStatus.OK,salesItemService.readItemPaged(page, limit));
    }

    @GetMapping("/{itemId}")
    public ApiResponse<ResponseSalesItemDto> getOneItem(@PathVariable("itemId") Long itemId) {
        return new ApiResponse<>(HttpStatus.OK,salesItemService.readOneItem(itemId));
    }

    @PutMapping("/{itemId}")
    public ApiResponse<ResponseDto> updateItem(
            @PathVariable("itemId") Long itemId, @RequestBody @Valid RequestSalesItemDto requestSalesItemDto) {
        salesItemService.update(itemId, requestSalesItemDto, getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK,getInstance(UPDATE_ITEM_MESSAGE));
    }

    @RequestMapping(value = "/{itemId}/image", method = {RequestMethod.PUT, RequestMethod.POST})
    public ApiResponse<ResponseDto> saveItemImage(
            @PathVariable("itemId") Long itemId, @RequestParam("image") MultipartFile image) {
        salesItemService.saveItemImage(itemId, image, getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK,getInstance(UPDATE_ITEM_IMAGE_MESSAGE));
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<ResponseDto> deleteItem(@PathVariable("itemId") Long itemId) {
        salesItemService.deleteItem(itemId, getCurrentUsername());
        return new ApiResponse<>(HttpStatus.OK,getInstance(DELETE_ITEM_MESSAGE));
    }
}

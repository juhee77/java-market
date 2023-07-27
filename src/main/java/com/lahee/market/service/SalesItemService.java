package com.lahee.market.service;

import com.lahee.market.dto.salesItem.RequestSalesItemDto;
import com.lahee.market.dto.salesItem.ResponseSalesItemDto;
import com.lahee.market.entity.SalesItem;
import com.lahee.market.entity.User;
import com.lahee.market.exception.ItemNotFoundException;
import com.lahee.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SalesItemService {
    private final SalesItemRepository salesItemRepository;
    private final UserService userService;

    @Transactional
    public ResponseSalesItemDto save(RequestSalesItemDto requestSalesItemDto, String username) {
        User user = userService.getUser(username);

        SalesItem salesItem = SalesItem.getEntityInstance(requestSalesItemDto, user);
        return ResponseSalesItemDto.fromEntity(salesItemRepository.save(salesItem));
    }

    public Page<ResponseSalesItemDto> readItemPaged(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("id"));
        Page<SalesItem> articleEntityPage = salesItemRepository.findAll(pageable);
        return articleEntityPage.map(ResponseSalesItemDto::fromEntity);
    }

    public ResponseSalesItemDto readOneItem(Long id) {
        SalesItem item = getSalesItem(id);
        return ResponseSalesItemDto.fromEntity(item);
    }

    @Transactional
    public void saveItemImage(Long id, MultipartFile image, String username) {
        //item 객체 찾기
        User user = userService.getUser(username);
        SalesItem item = getSalesItem(id);
        item.validItemIdInURL(user); //해당 유저가 제어건을 가지고 있는지 확인한다.

        // 폴더를 만든다.
        String profileDir = String.format("media/%d/", id);
        try {
            Files.createDirectories(Path.of(profileDir));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originalFilename = image.getOriginalFilename();
        assert originalFilename != null;
        String[] fileNameSplit = originalFilename.split("\\.");
        String extension = fileNameSplit[fileNameSplit.length - 1];
        String profilePath = profileDir + ("item." + extension);

        try {
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //이미지 위치를 저장한다.
        item.setImage(String.format("/static/%d/%s", id, "item." + extension));
    }

    @Transactional
    public void update(Long id, RequestSalesItemDto requestDto, String username) {
        User user = userService.getUser(username);
        SalesItem item = getSalesItem(id);

        item.validItemIdInURL(user); //해당 유저가 제어건을 가지고 있는지 확인한다.
        item.update(requestDto);
    }

    @Transactional
    public void deleteItem(Long id, String username) {
        SalesItem item = getSalesItem(id);

        User user = userService.getUser(username);
        item.validItemIdInURL(user);//해당 유저가 제어건을 가지고 있는지 확인한다.

        //연관관계중 제거
        user.removeItem(item);

        salesItemRepository.deleteById(id);
    }

    public SalesItem getSalesItem(Long itemId) {
        Optional<SalesItem> item = salesItemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ItemNotFoundException();
        }
        return item.get();
    }
}

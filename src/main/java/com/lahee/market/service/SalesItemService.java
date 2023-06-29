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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SalesItemService {
    private final SalesItemRepository salesItemRepository;

    @Transactional
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


    @Transactional
    public void saveItemImage(Long id, MultipartFile image, String writer, String password) {
        //item 객체 찾기
        SalesItem item = salesItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        //객체 작성자의 아이디 패스워드 일치 하는지 확인
        if (!item.getWriter().equals(writer)) {
            throw new RuntimeException("잘못된 아이디 입니다.");
        }
        if (!item.getPassword().equals(password)) {
            throw new RuntimeException("잘못된 패스워드 입니다.");
        }

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
            Path path = Path.of(profilePath);
            image.transferTo(path);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //이미지 위치를 저장한다.
        item.setImage(String.format("/static/%d/%s", id, "item." + extension));
    }

    @Transactional
    public void update(Long id, RequsetSalesItemDto requestDto) {
        //item 객체 찾기
        SalesItem item = salesItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("{}", requestDto);

        //객체 작성자의 아이디 패스워드 일치 하는지 확인
        if (!item.getWriter().equals(requestDto.getWriter())) {
            throw new RuntimeException("잘못된 아이디 입니다.");
        }
        if (!item.getPassword().equals(requestDto.getPassword())) {
            throw new RuntimeException("잘못된 패스워드 입니다.");
        }

        item.update(requestDto);
    }

}

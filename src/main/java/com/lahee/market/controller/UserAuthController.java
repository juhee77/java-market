package com.lahee.market.controller;

import com.lahee.market.dto.ResponseDto;
import com.lahee.market.dto.user.LoginDto;
import com.lahee.market.dto.user.SignupDto;
import com.lahee.market.dto.user.TokenDto;
import com.lahee.market.dto.user.UserResponseDto;
import com.lahee.market.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody LoginDto dto) {
        TokenDto tokenDto = userService.login(dto);
        return ResponseEntity.ok(ResponseDto.getInstance(tokenDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody SignupDto signupDto) {
        UserResponseDto userResponseDto = userService.signup(signupDto);
        return ResponseEntity.ok(ResponseDto.getInstance(userResponseDto));
    }
}

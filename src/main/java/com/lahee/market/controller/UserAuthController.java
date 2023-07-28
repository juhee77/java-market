package com.lahee.market.controller;

import com.lahee.market.dto.ApiResponse;
import com.lahee.market.dto.user.LoginDto;
import com.lahee.market.dto.user.SignupDto;
import com.lahee.market.dto.user.TokenDto;
import com.lahee.market.dto.user.UserResponseDto;
import com.lahee.market.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<TokenDto> login(@Valid @RequestBody LoginDto dto) {
        TokenDto tokenDto = userService.login(dto);
        return new ApiResponse<>(HttpStatus.OK,tokenDto);
    }

    @PostMapping("/signup")
    public ApiResponse<UserResponseDto> signup(@Valid @RequestBody SignupDto signupDto) {
        return new ApiResponse<>(HttpStatus.OK,userService.signup(signupDto));
    }
}

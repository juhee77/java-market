package com.lahee.market.controller;

import com.lahee.market.dto.ApiResponse;
import com.lahee.market.dto.user.LoginDto;
import com.lahee.market.dto.user.SignupDto;
import com.lahee.market.dto.user.TokenDto;
import com.lahee.market.dto.user.UserResponseDto;
import com.lahee.market.service.UserService;
import com.lahee.market.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/my/profile")
    public ApiResponse<UserResponseDto> login() {
        return new ApiResponse<>(HttpStatus.OK, userService.getUserDto(SecurityUtil.getCurrentUsername()));

    }
}

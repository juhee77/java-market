package com.lahee.market.service;

import com.lahee.market.dto.user.LoginDto;
import com.lahee.market.dto.user.SignupDto;
import com.lahee.market.dto.user.TokenDto;
import com.lahee.market.dto.user.UserResponseDto;
import com.lahee.market.entity.User;
import com.lahee.market.exception.CustomException;
import com.lahee.market.exception.ErrorCode;
import com.lahee.market.jwt.JwtTokenUtils;
import com.lahee.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public TokenDto login(LoginDto loginDto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
        TokenDto tokenDto = jwtTokenUtils.generateToken(userDetails);
        return tokenDto;
    }

    @Transactional
    public UserResponseDto signup(SignupDto signupDto) {
        User user = new User().builder()
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .build();

        return UserResponseDto.fromEntity(userRepository.save(user));
    }

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.SECURITY_INVALID_USER);
        }
        return user.get();
    }
}

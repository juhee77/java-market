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
        if (!passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        TokenDto tokenDto = jwtTokenUtils.generateToken(userDetails);
        return tokenDto;
    }

    @Transactional
    public UserResponseDto signup(SignupDto signupDto) {
        if (!signupDto.getPassword().equals(signupDto.getPasswordCheck())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_EQUAL);
        }
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_USERNAME);
        }

        //TODO 구현상의 오류로 임시로 로그인 아이디와 유저 이름을 동일하게 받도록 수정 , 이후에 채팅 부분 재 수정 예정
        if (!signupDto.getUsername().equals(signupDto.getNickname())) {
            throw new CustomException(ErrorCode.MY_FAULT);
        }

        User user = new User().builder()
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .nickname(signupDto.getNickname())
                .build();

        return UserResponseDto.fromEntity(userRepository.save(user));
    }

    public UserResponseDto getUserDto(String username) {
        return UserResponseDto.fromEntity(getUser(username));
    }

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.SECURITY_INVALID_USER);
        }
        return user.get();
    }

    public User getUserByNickName(String nickname) {
        log.info("NICKNAME" + nickname);
        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.SECURITY_INVALID_USER);
        }
        return user.get();
    }
}

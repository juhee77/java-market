package com.lahee.market.service;

import com.lahee.market.exception.UserNotFoundException;
import com.lahee.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) {
        Optional<com.lahee.market.entity.User> findedUser = userRepository.findByUsername(name);
        Optional<org.springframework.security.core.userdetails.User> user = findedUser.map(this::createUser);

        if (!findedUser.isPresent()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }

    private org.springframework.security.core.userdetails.User createUser(com.lahee.market.entity.User user) {
        log.info("CUSTOM USER DETAIL SERVICE 유저 생성!!");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

}

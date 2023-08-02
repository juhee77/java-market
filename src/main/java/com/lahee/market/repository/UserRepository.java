package com.lahee.market.repository;

import com.lahee.market.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String userName);
    Optional<User> findByNickname(String nickname);
}

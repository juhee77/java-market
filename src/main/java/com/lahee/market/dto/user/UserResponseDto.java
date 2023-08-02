package com.lahee.market.dto.user;

import com.lahee.market.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String nickname;

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.username = user.getUsername();
        userResponseDto.nickname = user.getNickname();
        return userResponseDto;
    }
}

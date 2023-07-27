package com.lahee.market.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDto {
    private String username;
    private String password;
    private String nickname;
    private String passwordCheck;
}

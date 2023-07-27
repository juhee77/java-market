package com.lahee.market.dto.user;

import lombok.Data;

@Data
public class SignupDto {
    private String username;
    private String password;
    private String nickname;
    private String passwordCheck;
}

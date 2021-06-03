package com.nikitarizh.testtask.dto.user;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String nickname;

    private String password;

    private String email;
}

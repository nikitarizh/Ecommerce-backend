package com.nikitarizh.testtask.dto.user;

import com.nikitarizh.testtask.entity.UserType;
import lombok.Data;

@Data
public class UserCreateDTO {
    private Integer id;

    private UserType userType;

    private String nickname;

    private String password;

    private String email;
}

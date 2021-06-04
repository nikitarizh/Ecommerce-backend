package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;

public interface UserService {

    UserFullDTO findById(Integer id);

    UserFullDTO create(UserCreateDTO userCreateDTO);
}

package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.UserNotFoundException;

public interface UserService {

    UserFullDTO findById(Integer id) throws UserNotFoundException;

    UserFullDTO create(UserCreateDTO userCreateDTO);
}

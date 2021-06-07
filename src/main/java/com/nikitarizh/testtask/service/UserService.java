package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;

import java.util.List;

public interface UserService {

    List<UserFullDTO> findAll();

    UserFullDTO findById(Integer id);

    User findByNickname(String nickname);

    UserFullDTO create(UserCreateDTO userCreateDTO);

    UserFullDTO addAdmin(Integer userId);
}

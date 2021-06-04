package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.UserNotFoundException;
import com.nikitarizh.testtask.repository.UserRepository;
import com.nikitarizh.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nikitarizh.testtask.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserFullDTO findById(Integer id) {
        return USER_MAPPER.mapToFullDTO(
                userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    @Override
    @Transactional
    public UserFullDTO create(UserCreateDTO userCreateDTO) {

        User newUser = userRepository.save(USER_MAPPER.mapToEntity(userCreateDTO));

        String hashedPassword = new BCryptPasswordEncoder().encode(userCreateDTO.getPassword());
        newUser.setPassword(hashedPassword);

        return USER_MAPPER.mapToFullDTO(newUser);
    }
}

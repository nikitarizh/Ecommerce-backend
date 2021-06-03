package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.UserNotFoundException;
import com.nikitarizh.testtask.mapper.UserMapper;
import com.nikitarizh.testtask.repository.UserRepository;
import com.nikitarizh.testtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.nikitarizh.testtask.mapper.UserMapper.USER_MAPPER;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserFullDTO findById(Integer id) throws UserNotFoundException {
        Optional<User> result = userRepository.findById(id);
        if (result.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return USER_MAPPER.mapToFullDTO(result.get());
    }

    @Override
    public UserFullDTO create(UserCreateDTO userCreateDTO) {
        User newUser = userRepository.save(USER_MAPPER.mapToEntity(userCreateDTO));
        return USER_MAPPER.mapToFullDTO(newUser);
    }
}

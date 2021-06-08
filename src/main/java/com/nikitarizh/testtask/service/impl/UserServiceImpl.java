package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.entity.UserRole;
import com.nikitarizh.testtask.exception.UserNotFoundException;
import com.nikitarizh.testtask.repository.UserRepository;
import com.nikitarizh.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.nikitarizh.testtask.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserFullDTO> findAll() {
        return userRepository.findAll().stream()
                .map(USER_MAPPER::mapToFullDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserFullDTO findById(Integer id) {
        User result = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return USER_MAPPER.mapToFullDTO(result);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException(nickname));
    }

    @Override
    @Transactional
    public UserFullDTO create(UserCreateDTO userCreateDTO) {

        User newUser = userRepository.save(USER_MAPPER.mapToEntity(userCreateDTO));

        String hashedPassword = bCryptPasswordEncoder.encode(userCreateDTO.getPassword());
        newUser.setPassword(hashedPassword);

        return USER_MAPPER.mapToFullDTO(newUser);
    }

    @Override
    @Transactional
    public UserFullDTO addAdmin(Integer userId) {

        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userToUpdate.setRole(UserRole.ROLE_ADMIN);

        return USER_MAPPER.mapToFullDTO(userToUpdate);
    }
}

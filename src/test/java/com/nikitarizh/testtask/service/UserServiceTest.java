package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.AbstractTest;
import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.entity.UserRole;
import com.nikitarizh.testtask.exception.UserNotFoundException;
import com.nikitarizh.testtask.utils.DataGenerator;
import com.nikitarizh.testtask.utils.DataManipulator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.nikitarizh.testtask.mapper.UserMapper.USER_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest extends AbstractTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(DataManipulator dataManipulator, UserService userService) {
        super(dataManipulator);

        this.userService = userService;
    }

    @Test
    @Transactional
    public void findById_happyPath() {
        // GIVEN
        User generatedUser = dataManipulator.saveUser(DataGenerator.generateValidUser());
        UserFullDTO generatedFullDTO = USER_MAPPER.mapToFullDTO(generatedUser);

        // WHEN
        UserFullDTO foundUserDTO = userService.findById(generatedUser.getId());

        // THEN
        assertEquals(generatedFullDTO, foundUserDTO);
    }

    @Test
    public void findById_notExists() {
        // GIVEN
        User generatedUser = dataManipulator.saveUser(DataGenerator.generateValidUser());

        // THEN
        assertThrows(UserNotFoundException.class, () -> userService.findById(generatedUser.getId() + 1));
    }

    @Test
    public void findByNickname_happyPath() {
        // GIVEN
        User generatedUser = dataManipulator.saveUser(DataGenerator.generateValidUser());

        // WHEN
        User foundUser = userService.findByNickname(generatedUser.getNickname());

        // THEN
        assertEquals(generatedUser, foundUser);
    }

    @Test
    public void findByNickname_notExists() {
        // GIVEN
        User generatedUser = dataManipulator.saveUser(DataGenerator.generateValidUser());

        // THEN
        assertThrows(UserNotFoundException.class, () -> userService.findByNickname(generatedUser.getNickname() + '%'));
    }

    @Test
    public void create_happyPath() {
        // GIVEN
        UserCreateDTO userCreateDTO = DataGenerator.generateValidUserCreateDTO();

        // WHEN
        UserFullDTO createdUserDTO = userService.create(userCreateDTO);

        // THEN
        assertEquals(userCreateDTO.getNickname(), createdUserDTO.getNickname());
        assertEquals(userCreateDTO.getEmail(), createdUserDTO.getEmail());
    }

    @Test
    public void addAdmin_happyPath() {
        // GIVEN
        User userToUpdate = dataManipulator.saveUser(DataGenerator.generateValidUser());

        // WHEN
        UserFullDTO updatedUserDTO = userService.addAdmin(userToUpdate.getId());

        // THEN
        assertEquals(UserRole.ROLE_ADMIN, updatedUserDTO.getRole());
    }

    @Test
    public void addAdmin_notExists() {
        // GIVEN
        User userToUpdate = dataManipulator.saveUser(DataGenerator.generateValidUser());

        // THEN
        assertThrows(UserNotFoundException.class, () -> userService.findById(userToUpdate.getId() + 1));
    }
}

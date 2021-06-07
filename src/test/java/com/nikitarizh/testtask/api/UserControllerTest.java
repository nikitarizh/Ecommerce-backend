package com.nikitarizh.testtask.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitarizh.testtask.AbstractTest;
import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.entity.UserRole;
import com.nikitarizh.testtask.utils.DataGenerator;
import com.nikitarizh.testtask.utils.DataManipulator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.nikitarizh.testtask.mapper.UserMapper.USER_MAPPER;

public class UserControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserControllerTest(DataManipulator dataManipulator, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(dataManipulator);

        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void registerUser_happyPath() throws Exception {
        // GIVEN
        UserCreateDTO userCreateDTO = DataGenerator.generateValidUserCreateDTO();

        // WHEN
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/users")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(asJsonString(userCreateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        UserFullDTO createdUser = objectMapper.readValue(content, UserFullDTO.class);

        // THEN
        assertEquals(userCreateDTO.getEmail(), createdUser.getEmail());
        assertEquals(userCreateDTO.getNickname(), createdUser.getNickname());
    }

    @Test
    public void registerUser_invalidDTO() throws Exception {
        // GIVEN
        UserCreateDTO userCreateDTO = DataGenerator.generateInvalidUserCreateDTO();

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userCreateDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void findAll_happyPath() throws Exception {
        // GIVEN
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        User user = dataManipulator.saveUser(DataGenerator.generateValidUser());
        List<UserFullDTO> requestedDTOs = new LinkedList<>();
        requestedDTOs.add(USER_MAPPER.mapToFullDTO(admin));
        requestedDTOs.add(USER_MAPPER.mapToFullDTO(user));
        String initialAdminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/users")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(initialAdminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        List<UserFullDTO> foundDTOs = objectMapper.readValue(content, new TypeReference<>() {});

        // THEN
        assertEquals(requestedDTOs, foundDTOs);
    }

    @Test
    public void findAll_unauthorized() throws Exception {
        // GIVEN
        dataManipulator.saveUser(DataGenerator.generateValidUser());

        // WHEN / THEN
        mockMvc.perform(get(BASE_URL + "/users"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void findAll_forbidden() throws Exception {
        // GIVEN
        User impostorAdmin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        String initialAdminCredentials = impostorAdmin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(get(BASE_URL + "/users")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(initialAdminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void findById_happyPath() throws Exception {
        // GIVEN
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        User user = dataManipulator.saveUser(DataGenerator.generateValidUser());
        UserFullDTO requestedDTO = USER_MAPPER.mapToFullDTO(user);
        String initialAdminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/users/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(initialAdminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        UserFullDTO foundDTO = objectMapper.readValue(content, UserFullDTO.class);

        // THEN
        assertEquals(requestedDTO, foundDTO);
    }

    @Test
    public void findById_notExists() throws Exception {
        // GIVEN
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        User user = dataManipulator.saveUser(DataGenerator.generateValidUser());
        String initialAdminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(get(BASE_URL + "/users/" + (user.getId() + 1))
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(initialAdminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void findById_unauthorized() throws Exception {
        // GIVEN
        User user = dataManipulator.saveUser(DataGenerator.generateValidUser());

        // WHEN / THEN
        mockMvc.perform(get(BASE_URL + "/users/" + (user.getId() + 1)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void findById_forbidden() throws Exception {
        // GIVEN
        User impostorAdmin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        User user = dataManipulator.saveUser(DataGenerator.generateValidUser());
        String initialAdminCredentials = impostorAdmin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(get(BASE_URL + "/users/" + user.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(initialAdminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void addAdmin_happyPath() throws Exception {
        // GIVEN
        User initialAdmin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        User adminPadawan = dataManipulator.saveUser(DataGenerator.generateValidUser());
        String initialAdminCredentials = initialAdmin.getNickname() + ":" + "rootroot1";

        // WHEN
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "/users/admins")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(initialAdminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(adminPadawan.getId())))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        UserFullDTO newAdmin = objectMapper.readValue(content, UserFullDTO.class);

        // THEN
        assertEquals(adminPadawan.getId(), newAdmin.getId());
        assertEquals(UserRole.ROLE_ADMIN, newAdmin.getRole());
    }

    @Test
    public void addAdmin_notExists() throws Exception {
        // GIVEN
        User initialAdmin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        User adminPadawan = dataManipulator.saveUser(DataGenerator.generateValidUser());
        String initialAdminCredentials = initialAdmin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/users/admins")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(initialAdminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(adminPadawan.getId() + 2)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addAdmin_unauthorized() throws Exception {
        // GIVEN
        User adminPadawan = dataManipulator.saveUser(DataGenerator.generateValidUser());

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/users/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(adminPadawan.getId() + 2)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void addAdmin_forbidden() throws Exception {
        // GIVEN
        User impostorAdmin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        User adminPadawan = dataManipulator.saveUser(DataGenerator.generateValidUser());
        String impostorAdminCredentials = impostorAdmin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/users/admins")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(impostorAdminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(adminPadawan.getId() + 2)))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}

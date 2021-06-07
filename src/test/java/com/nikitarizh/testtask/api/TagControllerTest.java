package com.nikitarizh.testtask.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitarizh.testtask.AbstractTest;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.Tag;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.TagNotFoundException;
import com.nikitarizh.testtask.service.TagService;
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
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.nikitarizh.testtask.mapper.TagMapper.TAG_MAPPER;

public class TagControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final TagService tagService;

    @Autowired
    public TagControllerTest(DataManipulator dataManipulator, MockMvc mockMvc,
                              ObjectMapper objectMapper, TagService tagService) {
        super(dataManipulator);

        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.tagService = tagService;
    }

    @Test
    public void findAll_happyPath() throws Exception {
        // GIVEN
        List<Tag> generatedTags = dataManipulator.saveTags(DataGenerator.generateValidTagsList());
        List<TagPreviewDTO> generatedDTOs = generatedTags.stream()
                .map(TAG_MAPPER::mapToPreviewDTO)
                .collect(Collectors.toList());

        // WHEN
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/tags"))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        List<TagPreviewDTO> foundDTOs = objectMapper.readValue(content, new TypeReference<>() {});

        // THEN
        assertEquals(generatedDTOs, foundDTOs);
    }

    @Test
    public void findAll_noData() throws Exception {
        // WHEN / THEN
        mockMvc.perform(get(BASE_URL + "/tags"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void create_happyPath() throws Exception {
        // GIVEN
        TagCreateDTO tagCreateDTO = DataGenerator.generateValidTagCreateDTO();
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/tags")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tagCreateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        TagPreviewDTO createdProduct = objectMapper.readValue(content, TagPreviewDTO.class);

        // THEN
        assertEquals(tagCreateDTO.getValue(), createdProduct.getValue());
    }

    @Test
    public void create_invalidDTO() throws Exception {
        // GIVEN
        TagCreateDTO tagCreateDTO = DataGenerator.generateInvalidTagCreateDTO();
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/tags")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tagCreateDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void create_unauthorized() throws Exception {
        // GIVEN
        TagCreateDTO tagCreateDTO = DataGenerator.generateValidTagCreateDTO();

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tagCreateDTO)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void create_forbidden() throws Exception {
        // GIVEN
        TagCreateDTO tagCreateDTO = DataGenerator.generateValidTagCreateDTO();
        User admin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/tags")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tagCreateDTO)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void delete_happyPathNotInUse() throws Exception {
        // GIVEN
        Tag tag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN
        mockMvc.perform(delete(BASE_URL + "/tags/" + tag.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertThrows(TagNotFoundException.class, () -> tagService.findById(tag.getId()));
    }

    @Test
    public void delete_happyPathInUseForce() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        Tag tag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setId(product.getId());
        updateDTO.setDescription(product.getDescription());
        List<Integer> tagIds = new LinkedList<>();
        tagIds.add(tag.getId());
        updateDTO.setTagIds(tagIds);

        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";
        mockMvc.perform(put(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        mockMvc.perform(delete(BASE_URL + "/tags/force/" + tag.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertThrows(TagNotFoundException.class, () -> tagService.findById(tag.getId()));
    }

    @Test
    public void delete_inCartNotForce() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        Tag tag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setId(product.getId());
        updateDTO.setDescription(product.getDescription());
        List<Integer> tagIds = new LinkedList<>();
        tagIds.add(tag.getId());
        updateDTO.setTagIds(tagIds);

        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";
        mockMvc.perform(put(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/tags/" + tag.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void delete_notExists() throws Exception {
        // GIVEN
        Tag tag = dataManipulator.saveTag(DataGenerator.generateValidTag());
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/tags/" + (tag.getId() + 1))
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void delete_unauthorized() throws Exception {
        // GIVEN
        Tag tag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/tags/" + tag.getId()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void delete_forbidden() throws Exception {
        // GIVEN
        Tag tag = dataManipulator.saveTag(DataGenerator.generateValidTag());
        User admin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/tags/" + tag.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}

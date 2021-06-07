package com.nikitarizh.testtask.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitarizh.testtask.AbstractTest;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.repository.ProductRepository;
import com.nikitarizh.testtask.repository.UserRepository;
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
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static com.nikitarizh.testtask.mapper.TagMapper.TAG_MAPPER;

public class CartControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartControllerTest(DataManipulator dataManipulator, MockMvc mockMvc,
                              ObjectMapper objectMapper, ProductRepository productRepository,
                              UserRepository userRepository) {
        super(dataManipulator);

        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Test
    public void addItem_happyPath() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User user = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String userCredentials = user.getNickname() + ":" + "rootroot1";

        // WHEN
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        ProductFullDTO responseDTO = objectMapper.readValue(content, ProductFullDTO.class);

        // THEN
        assertEquals(product.getId(), responseDTO.getId());
        assertEquals(product.getDescription(), responseDTO.getDescription());
        assertEquals(product.getTags().stream()
                .map(TAG_MAPPER::mapToPreviewDTO)
                .collect(Collectors.toList()), responseDTO.getTags());
        assertTrue(dataManipulator.getUserOrderedProductsById(user.getId()).contains(product));
        assertTrue(dataManipulator.getProductOrderByById(product.getId()).contains(user));
    }

    @Test
    public void addItem_notExists() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User user = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String userCredentials = user.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Integer.valueOf(product.getId() + 1).toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addItem_unauthorized() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void deleteItem_happyPath() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User user = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String userCredentials = user.getNickname() + ":" + "rootroot1";

        mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        mockMvc.perform(put(BASE_URL + "/carts/remove")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertFalse(dataManipulator.getUserOrderedProductsById(user.getId()).contains(product));
        assertFalse(dataManipulator.getProductOrderByById(product.getId()).contains(user));
    }

    @Test
    public void deleteItem_notExists() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User user = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String userCredentials = user.getNickname() + ":" + "rootroot1";

        mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/carts/remove")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Integer.valueOf(product.getId() + 1).toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void deleteItem_unauthorized() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/carts/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void buyItems_happyPath() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User user = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String userCredentials = user.getNickname() + ":" + "rootroot1";

         mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        mockMvc.perform(put(BASE_URL + "/carts/buy")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertTrue(dataManipulator.getUserOrderedProductsById(user.getId()).isEmpty());
    }

    @Test
    public void buyItems_cartIsEmpty() throws Exception {
        // GIVEN
        User user = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String userCredentials = user.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/carts/buy")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void buyItems_unauthorized() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/carts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}

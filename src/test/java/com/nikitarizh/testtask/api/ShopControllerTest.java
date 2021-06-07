package com.nikitarizh.testtask.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitarizh.testtask.AbstractTest;
import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.service.ProductService;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;

public class ShopControllerTest extends AbstractTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ProductService productService;

    @Autowired
    public ShopControllerTest(DataManipulator dataManipulator, MockMvc mockMvc,
                              ObjectMapper objectMapper, ProductService productService) {
        super(dataManipulator);

        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @Test
    public void findAll_happyPath() throws Exception {
        // GIVEN
        List<Product> generatedProducts = dataManipulator.saveProducts(DataGenerator.generateValidProductsList());
        List<ProductFullDTO> generatedDTOs = generatedProducts.stream()
                .map(PRODUCT_MAPPER::mapToFullDTO)
                .collect(Collectors.toList());

        // WHEN
        MvcResult mvcResult = mockMvc.perform(get(BASE_URL + "/shops"))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        List<ProductFullDTO> foundProducts = objectMapper.readValue(content, new TypeReference<>() {});

        // THEN
        assertEquals(generatedDTOs, foundProducts);
    }

    @Test
    public void findAll_noData() throws Exception {
        // WHEN / THEN
        mockMvc.perform(get(BASE_URL + "/shops"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void create_happyPath() throws Exception {
        // GIVEN
        ProductCreateDTO productCreateDTO = DataGenerator.generateValidProductCreateDTO();
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN
        MvcResult mvcResult = mockMvc.perform(post(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productCreateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        ProductFullDTO createdProduct = objectMapper.readValue(content, ProductFullDTO.class);

        // THEN
        assertEquals(productCreateDTO.getDescription(), createdProduct.getDescription());
        assertEquals(productCreateDTO.getTagIds(), createdProduct.getTags().stream()
                .map(TagPreviewDTO::getId)
                .collect(Collectors.toList()));
    }

    @Test
    public void create_invalidDTO() throws Exception {
        // GIVEN
        ProductCreateDTO productCreateDTO = DataGenerator.generateInvalidProductCreateDTO();
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productCreateDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void create_unauthorized() throws Exception {
        // GIVEN
        ProductCreateDTO productCreateDTO = DataGenerator.generateValidProductCreateDTO();

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productCreateDTO)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void create_forbidden() throws Exception {
        // GIVEN
        ProductCreateDTO productCreateDTO = DataGenerator.generateValidProductCreateDTO();
        User admin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(post(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productCreateDTO)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void update_happyPathNotInCart() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(product.getId());

        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productUpdateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        ProductFullDTO updatedProduct = objectMapper.readValue(content, ProductFullDTO.class);

        // THEN
        assertEquals(productUpdateDTO.getId(), updatedProduct.getId());
        assertEquals(productUpdateDTO.getTagIds(), updatedProduct.getTags().stream()
                .map(TagPreviewDTO::getId)
                .collect(Collectors.toList()));
        assertEquals(productUpdateDTO.getDescription(), updatedProduct.getDescription());
    }

    @Test
    public void update_happyPathInCartForce() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(product.getId());
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";
        mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(productUpdateDTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        MvcResult mvcResult = mockMvc.perform(put(BASE_URL + "/shops/force")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productUpdateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        byte[] content = mvcResult.getResponse().getContentAsByteArray();
        ProductFullDTO updatedProduct = objectMapper.readValue(content, ProductFullDTO.class);

        // THEN
        assertEquals(productUpdateDTO.getId(), updatedProduct.getId());
        assertEquals(productUpdateDTO.getTagIds(), updatedProduct.getTags().stream()
                .map(TagPreviewDTO::getId)
                .collect(Collectors.toList()));
        assertEquals(productUpdateDTO.getDescription(), updatedProduct.getDescription());
    }

    @Test
    public void update_inCartNotForce() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(product.getId());
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";
        mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(productUpdateDTO.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void update_invalidDTO() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        ProductUpdateDTO productUpdateDTO = DataGenerator.generateInvalidProductUpdateDTO();
        productUpdateDTO.setId(product.getId());
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void update_unauthorized() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(product.getId());

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productUpdateDTO)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void update_forbidden() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(product.getId());
        User admin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(put(BASE_URL + "/shops")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productUpdateDTO)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void delete_happyPathNotInCart() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN
        mockMvc.perform(delete(BASE_URL + "/shops/" + product.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertThrows(ProductNotFoundException.class, () -> productService.findById(product.getId()));
    }

    @Test
    public void delete_happyPathInCartForce() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";
        mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        mockMvc.perform(delete(BASE_URL + "/shops/force/" + product.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        assertThrows(ProductNotFoundException.class, () -> productService.findById(product.getId()));
    }

    @Test
    public void delete_inCartNotForce() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";
        mockMvc.perform(post(BASE_URL + "/carts")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(product.getId().toString()))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/shops/" + product.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void delete_notExists() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User admin = dataManipulator.saveAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/shops/" + (product.getId() + 1))
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void delete_unauthorized() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/shops/" + product.getId()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void delete_forbidden() throws Exception {
        // GIVEN
        Product product = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        User admin = dataManipulator.saveImpostorAdmin(DataGenerator.generateValidUser());
        String adminCredentials = admin.getNickname() + ":" + "rootroot1";

        // WHEN / THEN
        mockMvc.perform(delete(BASE_URL + "/shops/" + product.getId())
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString(adminCredentials.getBytes(StandardCharsets.UTF_8))))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}

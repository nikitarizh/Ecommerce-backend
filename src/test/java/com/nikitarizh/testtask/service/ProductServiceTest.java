package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.AbstractTest;
import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.dto.tag.TagPreviewDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.Tag;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.exception.TagNotFoundException;
import com.nikitarizh.testtask.utils.DataGenerator;
import com.nikitarizh.testtask.utils.DataManipulator;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductServiceTest extends AbstractTest {

    private final ProductService productService;

    @Autowired
    public ProductServiceTest(DataManipulator dataManipulator, ProductService productService) {
        super(dataManipulator);

        this.productService = productService;
    }

    @Test
    @Transactional
    public void findAll_happyPath() {
        // GIVEN
        List<Product> generatedProducts = dataManipulator.saveProducts(DataGenerator.generateValidProductsList());
        Hibernate.initialize(generatedProducts);
        List<ProductFullDTO> generatedDTOs = generatedProducts.stream()
                .map(PRODUCT_MAPPER::mapToFullDTO)
                .collect(Collectors.toList());

        // WHEN
        List<ProductFullDTO> foundDTOs = productService.search(null, null);

        // THEN
        assertEquals(generatedDTOs, foundDTOs);
    }

    @Test
    @Transactional
    public void findById_happyPath() {
        // GIVEN
        Product generatedProduct = dataManipulator.saveProduct(DataGenerator.generateValidProduct());
        ProductFullDTO generatedDTO = PRODUCT_MAPPER.mapToFullDTO(generatedProduct);

        // WHEN
        ProductFullDTO foundDTO = productService.findById(generatedProduct.getId());

        // THEN
        assertEquals(generatedDTO, foundDTO);
    }

    @Test
    public void findById_notExists() {
        // GIVEN
        Product generatedProduct = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        // THEN
        assertThrows(ProductNotFoundException.class, () -> productService.findById(generatedProduct.getId() + 1));
    }

    @Test
    public void create_happyPath() {
        // GIVEN
        ProductCreateDTO productCreateDTO = DataGenerator.generateValidProductCreateDTO();

        // WHEN
        ProductFullDTO createdProduct = productService.create(productCreateDTO);

        // THEN
        assertEquals(productCreateDTO.getDescription(), createdProduct.getDescription());
    }

    @Test
    @Transactional
    public void update_happyPathNotInCart() {
        // GIVEN
        Product productToUpdate = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        Tag generatedTag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(productToUpdate.getId());
        List<Integer> tagIds = new LinkedList<>();
        tagIds.add(generatedTag.getId());
        productUpdateDTO.setTagIds(tagIds);

        // WHEN
        ProductFullDTO updatedProduct = productService.update(productUpdateDTO, false);

        // THEN
        assertEquals(productUpdateDTO.getDescription(), updatedProduct.getDescription());
        assertEquals(productUpdateDTO.getTagIds(), updatedProduct.getTags().stream()
                .map(TagPreviewDTO::getId)
                .collect(Collectors.toList()));
    }

    @Test
    public void update_productNotExists() {
        // GIVEN
        Tag generatedTag = dataManipulator.saveTag(DataGenerator.generateValidTag());

        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(1);
        List<Integer> tagIds = new LinkedList<>();
        tagIds.add(generatedTag.getId());
        productUpdateDTO.setTagIds(tagIds);

        // THEN
        assertThrows(ProductNotFoundException.class, () -> productService.update(productUpdateDTO, false));
    }

    @Test
    public void update_tagNotExists() {
        // GIVEN
        Product productToUpdate = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(productToUpdate.getId());
        List<Integer> tagIds = new LinkedList<>();
        tagIds.add(1);
        productUpdateDTO.setTagIds(tagIds);

        // THEN
        assertThrows(TagNotFoundException.class, () -> productService.update(productUpdateDTO, false));
    }

    @Test
    public void delete_happyPathNotInCart() {
        // GIVEN
        Product productToDelete = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(productToDelete.getId());
        List<Integer> tagIds = new LinkedList<>();
        tagIds.add(productToDelete.getId());
        productUpdateDTO.setTagIds(tagIds);

        // WHEN
        productService.delete(productToDelete.getId(), false);

        // THEN
        assertThrows(ProductNotFoundException.class, () -> productService.findById(productToDelete.getId()));
    }

    @Test
    public void delete_notExists() {
        // GIVEN
        Product productToDelete = dataManipulator.saveProduct(DataGenerator.generateValidProduct());

        ProductUpdateDTO productUpdateDTO = DataGenerator.generateValidProductUpdateDTO();
        productUpdateDTO.setId(productToDelete.getId());
        List<Integer> tagIds = new LinkedList<>();
        tagIds.add(productToDelete.getId());
        productUpdateDTO.setTagIds(tagIds);

        // THEN
        assertThrows(ProductNotFoundException.class, () -> productService.findById(productToDelete.getId() + 1));
    }

    @Test
    @Transactional
    public void search_happyPath() {
        // GIVEN
        final String descriptionToSearchFor = "searching for";

        List<Product> products = DataGenerator.generateValidProductsList();
        List<Tag> generatedTags = dataManipulator.saveTags(DataGenerator.generateValidTagsList());

        products.get(0).setDescription(descriptionToSearchFor);
        products.get(0).setTags(generatedTags.stream()
                .filter(tag -> tag.getId() % 2 == 0)
                .collect(Collectors.toList()));
        products = dataManipulator.saveProducts(products);

        // WHEN
        List<ProductFullDTO> foundProducts = productService.search(descriptionToSearchFor, generatedTags.stream()
                .map(Tag::getId)
                .collect(Collectors.toList()));

        // THEN
        List<ProductFullDTO> expected = new LinkedList<>();
        expected.add(PRODUCT_MAPPER.mapToFullDTO(products.get(0)));
        assertEquals(expected, foundProducts);
    }

    @Test
    @Transactional
    public void search_noResults() {
        // GIVEN
        final String descriptionToSearchFor = "searching for";

        List<Product> products = DataGenerator.generateValidProductsList();
        List<Tag> generatedTags = dataManipulator.saveTags(DataGenerator.generateValidTagsList());

        products.get(0).setDescription(descriptionToSearchFor);
        products.get(0).setTags(generatedTags.stream()
                .filter(tag -> tag.getId() % 2 == 0)
                .collect(Collectors.toList()));
        dataManipulator.saveProducts(products);

        // THEN
        assertThrows(ProductNotFoundException.class, () -> productService.search(descriptionToSearchFor + '%',
                generatedTags.stream()
                        .map((tag) -> tag.getId() + 100)
                        .collect(Collectors.toList())));
    }
}

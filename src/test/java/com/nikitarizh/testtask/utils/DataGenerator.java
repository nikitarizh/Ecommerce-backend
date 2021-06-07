package com.nikitarizh.testtask.utils;

import com.github.javafaker.Faker;
import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.dto.tag.TagCreateDTO;
import com.nikitarizh.testtask.dto.user.UserCreateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.Tag;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.entity.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class DataGenerator {

    private static final Faker FAKER = Faker.instance();
    private static final int LIST_DEFAULT_SIZE = 5;

    // ------- USER -------

    public static User generateValidUser() {
        User generatedUser = new User();
        generatedUser.setNickname(FAKER.name().firstName());
        generatedUser.setPassword(new BCryptPasswordEncoder().encode("passw0rd"));
        generatedUser.setRole(UserRole.ROLE_USER);
        generatedUser.setEmail("bob@gmail.com");
        generatedUser.setOrderedProducts(new ArrayList<>());

        return generatedUser;
    }

    public static UserCreateDTO generateValidUserCreateDTO() {
        UserCreateDTO generatedDTO = new UserCreateDTO();
        generatedDTO.setNickname(FAKER.name().firstName());
        generatedDTO.setEmail("bob@gmail.com");
        generatedDTO.setPassword("passw0rd");

        return generatedDTO;
    }

    public static UserCreateDTO generateInvalidUserCreateDTO() {
        UserCreateDTO generatedDTO = new UserCreateDTO();
        generatedDTO.setNickname(null);
        generatedDTO.setEmail("bob");
        generatedDTO.setPassword("12345678");

        return generatedDTO;
    }

    // ------- TAG -------

    public static Tag generateValidTag() {
        Tag generatedTag = new Tag();
        generatedTag.setValue(FAKER.rickAndMorty().location());
        generatedTag.setProducts(new ArrayList<>());

        return generatedTag;
    }

    public static List<Tag> generateValidTagsList() {
        List<Tag> output = new ArrayList<>();
        for (int i = 0; i < LIST_DEFAULT_SIZE; i++) {
            output.add(generateValidTag());
        }

        return output;
    }

    public static TagCreateDTO generateValidTagCreateDTO() {
        TagCreateDTO generatedDTO = new TagCreateDTO();
        generatedDTO.setValue(FAKER.rickAndMorty().location());

        return generatedDTO;
    }

    public static TagCreateDTO generateInvalidTagCreateDTO() {
        TagCreateDTO generatedDTO = new TagCreateDTO();
        generatedDTO.setValue("");

        return generatedDTO;
    }

    // ------- PRODUCT -------

    public static Product generateValidProduct() {
        Product generatedProduct = new Product();
        generatedProduct.setDescription(FAKER.rickAndMorty().quote());
        generatedProduct.setTags(new ArrayList<>());
        generatedProduct.setOrderedBy(new ArrayList<>());

        return generatedProduct;
    }

    public static List<Product> generateValidProductsList() {
        List<Product> output = new ArrayList<>();
        for (int i = 0; i < LIST_DEFAULT_SIZE; i++) {
            output.add(generateValidProduct());
        }

        return output;
    }

    public static ProductCreateDTO generateValidProductCreateDTO() {
        ProductCreateDTO generatedDTO = new ProductCreateDTO();
        generatedDTO.setDescription(FAKER.rickAndMorty().quote());
        generatedDTO.setTagIds(new ArrayList<>());

        return generatedDTO;
    }

    public static ProductCreateDTO generateInvalidProductCreateDTO() {
        ProductCreateDTO generatedDTO = new ProductCreateDTO();
        generatedDTO.setDescription("");
        generatedDTO.setTagIds(null);
        generatedDTO.setTagIds(new LinkedList<>());

        return generatedDTO;
    }

    public static ProductUpdateDTO generateValidProductUpdateDTO() {
        ProductUpdateDTO generatedDTO = new ProductUpdateDTO();
        generatedDTO.setDescription(FAKER.rickAndMorty().character());
        generatedDTO.setTagIds(new LinkedList<>());

        return generatedDTO;
    }

    public static ProductUpdateDTO generateInvalidProductUpdateDTO() {
        ProductUpdateDTO generatedDTO = new ProductUpdateDTO();
        generatedDTO.setDescription("   ");
        generatedDTO.setTagIds(new LinkedList<>());

        return generatedDTO;
    }
}

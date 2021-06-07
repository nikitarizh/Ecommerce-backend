package com.nikitarizh.testtask.utils;

import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.Tag;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.entity.UserRole;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.exception.UserNotFoundException;
import com.nikitarizh.testtask.repository.ProductRepository;
import com.nikitarizh.testtask.repository.TagRepository;
import com.nikitarizh.testtask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataManipulator {

    protected final UserRepository userRepository;
    protected final TagRepository tagRepository;
    protected final ProductRepository productRepository;

    @Transactional
    public void clearAllDatabases() {
        userRepository.deleteAll();
        productRepository.deleteAll();
        tagRepository.deleteAll();
    }

    // ------- USER -------

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User saveAdmin(User user) {
        user.setRole(UserRole.ROLE_ADMIN);
        user.setPassword(new BCryptPasswordEncoder().encode("rootroot1"));

        return userRepository.save(user);
    }

    @Transactional
    public User saveImpostorAdmin(User user) {
        user.setRole(UserRole.ROLE_USER);
        user.setPassword(new BCryptPasswordEncoder().encode("rootroot1"));

        return userRepository.save(user);
    }

    @Transactional
    public List<Product> getUserOrderedProductsById(Integer userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Hibernate.initialize(foundUser.getOrderedProducts());

        return foundUser.getOrderedProducts();
    }

    // ------- TAG -------

    @Transactional
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    public List<Tag> saveTags(Iterable<Tag> tags) {
        return tagRepository.saveAll(tags);
    }

    // ------- PRODUCT -------

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public List<Product> saveProducts(Iterable<Product> products) {
        return productRepository.saveAll(products);
    }

    @Transactional
    public List<User> getProductOrderByById(Integer productId) {
        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Hibernate.initialize(foundProduct.getOrderedBy());

        return foundProduct.getOrderedBy();
    }
}

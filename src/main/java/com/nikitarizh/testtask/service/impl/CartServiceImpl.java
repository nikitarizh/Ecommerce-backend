package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.CartIsEmptyException;
import com.nikitarizh.testtask.exception.ProductAlreadyInCartException;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.repository.ProductRepository;
import com.nikitarizh.testtask.repository.UserRepository;
import com.nikitarizh.testtask.service.AuthService;
import com.nikitarizh.testtask.service.CartService;
import com.nikitarizh.testtask.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final MailService mailService;
    private final AuthService authService;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProductFullDTO addItem(Integer productId) {
        Product requestedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        User user = authService.getAuthenticatedUser();

        if (user.getOrderedProducts().contains(requestedProduct)) {
            throw new ProductAlreadyInCartException(requestedProduct, user);
        }

        user.getOrderedProducts().add(requestedProduct);

        return PRODUCT_MAPPER.mapToFullDTO(requestedProduct);
    }

    @Override
    @Transactional
    public void removeItem(Integer productId) {
        Product requestedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        User user = authService.getAuthenticatedUser();

        user.getOrderedProducts().remove(requestedProduct);
    }

    @Override
    @Transactional
    public void buyItems() {
        User user = authService.getAuthenticatedUser();

        if (user.getOrderedProducts().size() == 0) {
            throw new CartIsEmptyException();
        }

        mailService.sendBuyNotification(user);

        user.getOrderedProducts().clear();
    }
}

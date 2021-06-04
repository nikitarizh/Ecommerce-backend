package com.nikitarizh.testtask.service.impl;

import com.nikitarizh.testtask.dto.order.OrderChangeDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;
import com.nikitarizh.testtask.exception.ProductAlreadyInCartException;
import com.nikitarizh.testtask.exception.ProductNotFoundException;
import com.nikitarizh.testtask.exception.UserNotFoundException;
import com.nikitarizh.testtask.repository.ProductRepository;
import com.nikitarizh.testtask.repository.UserRepository;
import com.nikitarizh.testtask.service.CartService;
import com.nikitarizh.testtask.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nikitarizh.testtask.mapper.ProductMapper.PRODUCT_MAPPER;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final MailService mailService;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ProductFullDTO addItem(OrderChangeDTO orderChangeDTO) {
        Product requestedProduct = productRepository.findById(orderChangeDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(orderChangeDTO.getProductId()));
        User user = userRepository.findById(orderChangeDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(orderChangeDTO.getUserId()));

        if (user.getOrderedProducts().contains(requestedProduct)) {
            throw new ProductAlreadyInCartException(requestedProduct, user);
        }

        user.getOrderedProducts().add(requestedProduct);
        requestedProduct.getOrderedBy().add(user);

        return PRODUCT_MAPPER.mapToFullDTO(requestedProduct);
    }

    @Override
    @Transactional
    public void removeItem(OrderChangeDTO orderChangeDTO) {
        Product requestedProduct = productRepository.findById(orderChangeDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(orderChangeDTO.getProductId()));
        User user = userRepository.findById(orderChangeDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(orderChangeDTO.getUserId()));

        user.getOrderedProducts().remove(requestedProduct);
        requestedProduct.getOrderedBy().remove(user);
    }

    @Override
    @Transactional
    public void buyItems(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        mailService.sendBuyNotification(user);

        user.getOrderedProducts().clear();
    }
}

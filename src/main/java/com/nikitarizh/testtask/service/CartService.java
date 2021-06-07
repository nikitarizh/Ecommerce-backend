package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;

import java.util.List;

public interface CartService {

    List<ProductFullDTO> getCart();

    ProductFullDTO addItem(Integer productId);

    void removeItem(Integer productId);

    void buyItems();
}

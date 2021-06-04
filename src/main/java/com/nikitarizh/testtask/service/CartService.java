package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;

public interface CartService {

    ProductFullDTO addItem(Integer productId);

    void removeItem(Integer productId);

    void buyItems();
}

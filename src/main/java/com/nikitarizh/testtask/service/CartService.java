package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.order.OrderChangeDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;

public interface CartService {

    ProductFullDTO addItem(OrderChangeDTO orderChangeDTO);

    void removeItem(OrderChangeDTO orderChangeDTO);

    void buyItems(Integer userId);
}

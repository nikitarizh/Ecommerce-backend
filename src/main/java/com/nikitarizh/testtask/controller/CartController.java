package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.order.OrderChangeDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.user.UserFullDTO;
import com.nikitarizh.testtask.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ProductFullDTO addItem(@RequestBody OrderChangeDTO orderChangeDTO) {
        return cartService.addItem(orderChangeDTO);
    }

    @DeleteMapping("/remove")
    public void removeItem(@RequestBody OrderChangeDTO orderChangeDTO) {
        cartService.removeItem(orderChangeDTO);
    }

    @PostMapping("/buy")
    public void buyItems(@RequestBody UserFullDTO userFullDTO) {
        cartService.buyItems(userFullDTO);
    }
}

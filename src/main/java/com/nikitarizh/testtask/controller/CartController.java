package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ProductFullDTO addItem(@RequestBody Integer productId) {
        return cartService.addItem(productId);
    }

    @PutMapping("/remove")
    public void removeItem(@RequestBody Integer productId) {
        cartService.removeItem(productId);
    }

    @PutMapping("/buy")
    public void buyItems() {
        cartService.buyItems();
    }
}

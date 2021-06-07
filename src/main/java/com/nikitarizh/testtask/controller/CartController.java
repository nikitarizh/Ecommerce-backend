package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Api(description = "Controller to order and buy products")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @ApiOperation(value = "Get cart of authenticated user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 401, message = "Unauthorized access")
    })
    public List<ProductFullDTO> getCart() {
        return cartService.getCart();
    }

    @PostMapping
    @ApiOperation(value = "Add product to the cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item is removed"),
            @ApiResponse(code = 400, message = "Item is already in cart"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    public ProductFullDTO addItem(@RequestBody Integer productId) {
        return cartService.addItem(productId);
    }

    @PutMapping("/remove")
    @ApiOperation(value = "Remove item from the cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item is removed"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 404, message = "Product was not in cart or doesn't exist")
    })
    public void removeItem(@RequestBody Integer productId) {
        cartService.removeItem(productId);
    }

    @PutMapping("/buy")
    @ApiOperation(value = "Buy all items that are in the cart")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Items are bought"),
            @ApiResponse(code = 400, message = "Cart is empty"),
            @ApiResponse(code = 401, message = "Unauthorized access")
    })
    public void buyItems() {
        cartService.buyItems();
    }
}

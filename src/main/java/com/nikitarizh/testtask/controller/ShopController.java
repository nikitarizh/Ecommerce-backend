package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;

    @Autowired
    public ShopController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<ProductFullDTO> getAll() {
        return productService.findAll();
    }

    @PostMapping
    public ProductFullDTO create(@RequestBody ProductCreateDTO productCreateDTO) {
        return productService.create(productCreateDTO);
    }

    @PutMapping
    public ProductFullDTO update(@RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.update(productUpdateDTO, false);
    }

    @PutMapping("/force")
    public ProductFullDTO forceUpdate(@RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.update(productUpdateDTO, true);
    }

    @DeleteMapping("/{productId}")
    public void delete(@PathVariable Integer productId) {
        productService.delete(productId);
    }
}

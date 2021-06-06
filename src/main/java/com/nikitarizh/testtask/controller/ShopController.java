package com.nikitarizh.testtask.controller;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
@Api(description = "Controller to search, create, update and delete products")
public class ShopController {

    private final ProductService productService;

    @GetMapping()
    @ApiOperation(value = "Get products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 404, message = "Products with specified params not found")
    })
    public Iterable<ProductFullDTO> getAll(@RequestParam(required = false) String description,
                                           @RequestParam(required = false) List<Integer> tagIds) {
        return productService.search(description, tagIds);
    }

    @PostMapping
    @ApiOperation(value = "Create product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product is created"),
            @ApiResponse(code = 400, message = "Bad DTO"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
    })
    public ProductFullDTO create(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        return productService.create(productCreateDTO);
    }

    @PutMapping
    @ApiOperation(value = "Update product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item is updated"),
            @ApiResponse(code = 400, message = "Bad DTO or item is in cart of at least one user"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    public ProductFullDTO update(@Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.update(productUpdateDTO, false);
    }

    @PutMapping("/force")
    @ApiOperation(value = "Force update product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item is updated"),
            @ApiResponse(code = 400, message = "Bad DTO"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    public ProductFullDTO forceUpdate(@Valid @RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.update(productUpdateDTO, true);
    }

    @DeleteMapping("/{productId}")
    @ApiOperation(value = "Delete product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item is deleted"),
            @ApiResponse(code = 400, message = "Bad DTO or item is in cart of at least one user"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    public void delete(@PathVariable Integer productId) {
        productService.delete(productId, false);
    }

    @DeleteMapping("/force/{productId}")
    @ApiOperation(value = "Force delete product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item is deleted"),
            @ApiResponse(code = 400, message = "Bad DTO"),
            @ApiResponse(code = 401, message = "Unauthorized access"),
            @ApiResponse(code = 404, message = "Product with specified id doesn't exist")
    })
    public void forceDelete(@PathVariable Integer productId) {
        productService.delete(productId, true);
    }
}

package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductDeleteDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;
import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {

    ProductFullDTO findById(Integer id) throws ProductNotFoundException;

    Iterable<ProductFullDTO> findAll();

    ProductFullDTO create(ProductCreateDTO productCreateDTO);

    ProductFullDTO update(ProductUpdateDTO productUpdateDTO, boolean force);

    void delete(ProductDeleteDTO productDeleteDTO);
}

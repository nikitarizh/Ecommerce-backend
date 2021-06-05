package com.nikitarizh.testtask.service;

import com.nikitarizh.testtask.dto.product.ProductCreateDTO;
import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import com.nikitarizh.testtask.dto.product.ProductUpdateDTO;

import java.util.List;

public interface ProductService {

    ProductFullDTO findById(Integer id);

    List<ProductFullDTO> findAll();

    ProductFullDTO create(ProductCreateDTO productCreateDTO);

    ProductFullDTO update(ProductUpdateDTO productUpdateDTO, boolean force);

    List<ProductFullDTO> search(String description, List<Integer> tagIds);

    void delete(Integer productId);
}

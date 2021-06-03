package com.nikitarizh.testtask.dto.tag;

import com.nikitarizh.testtask.dto.product.ProductFullDTO;
import lombok.Data;

import java.util.List;

@Data
public class TagFullDTO {
    private String value;

    private List<ProductFullDTO> products;
}
